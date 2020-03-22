package de.pandemieduell.model;

import de.pandemieduell.api.exceptions.UnprocessableEntryException;
import java.security.SecureRandom;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.web.server.ServerErrorException;

public class Duel {
  @Id
  @Field("id")
  private String id;

  @Field("government_player")
  private Player governmentPlayer;

  @Field("pandemic_player")
  private Player pandemicPlayer;

  @Field("game_state")
  private GameState gameState;

  @Field("card_deck")
  private CardDeck cardDeck;

  @Field("rounds")
  private List<Round> rounds;

  private static SecureRandom random = new SecureRandom();

  public int getRoundNumber() {
    return rounds.size() - 1;
  }

  public Duel(Player governmentPlayer) {
    this.id = UUID.randomUUID().toString();
    this.rounds = new LinkedList<>();
    this.governmentPlayer = governmentPlayer;
    this.gameState = GameState.INCOMPLETE;

    this.cardDeck = new CardDeck();
    // TODO: Insert all cards to the deck.
  }

  private static <T extends Card> T drawCard(List<T> cards, WorldState state, Integer roundNumber) {
    List<Integer> ticketNumbers =
        cards
            .stream()
            .map(a -> a.getNumberOfTickets(roundNumber, state))
            .collect(Collectors.toList());
    int totalTicketNumber = ticketNumbers.stream().mapToInt(Integer::intValue).sum();

    int currentBase = 0;
    Map<ValueRange, Integer> ranges = new HashMap<>();
    for (int i = 0; i < ticketNumbers.size(); ++i) {
      int currentUpper = currentBase + ticketNumbers.get(i);
      ranges.put(ValueRange.of(currentBase, currentUpper - 1), i);
      currentBase = currentUpper;
    }

    int drawnTicket = random.nextInt(totalTicketNumber);
    int pickedCard =
        ranges
            .entrySet()
            .stream()
            .filter(x -> x.getKey().isValidValue(drawnTicket))
            .mapToInt(Map.Entry::getValue)
            .findFirst()
            .orElse(0);
    return cards.remove(pickedCard);
  }

  private void initializeDuel() {
    StandardWorldState worldState = new StandardWorldState();
    Round round = new Round(0, worldState);
    // Draw 4 cards for each player
    for (int i = 0; i < 4; i++) {
      round.getPandemicCards().add(drawCard(cardDeck.getPandemicCards(), worldState, 0));
      round.getGovernmentCards().add(drawCard(cardDeck.getGovernmentCards(), worldState, 0));
    }
  }

  // to be called when both players made their turn and the game can advance to the next round
  private void finishRound() throws CloneNotSupportedException {
    Round lastRound = this.rounds.get(this.getRoundNumber());
    // draw event card and add to played cards
    lastRound
        .getPlayedCards()
        .add(
            this.getRoundNumber(),
            drawCard(
                cardDeck.getEventCards(), lastRound.getWorldState(), lastRound.getRoundNumber()));

    // find applicable actions
    List<GameAction> actionsToExecute =
        this.rounds
            .parallelStream()
            .map(Round::getPlayedCards)
            .flatMap(List::stream)
            .map(Card::getGameActions)
            .flatMap(List::stream)
            .filter(x -> x.isApplicable(this.getRoundNumber(), lastRound.getWorldState()))
            .collect(Collectors.toList());

    // execute applicable actions
    StandardWorldState action_state = (StandardWorldState) lastRound.getWorldState().clone();
    for (GameAction action : actionsToExecute) {
      action.updateWorldState(action_state);
    }
    lastRound.getExecutedActions().addAll(actionsToExecute);

    // create new round
    Round nextRound = new Round(this.getRoundNumber() + 1, (StandardWorldState) action_state);

    // transfer cards to keep next round
    nextRound.getGovernmentCards().add(lastRound.getGovernmentCards().remove(random.nextInt(3)));
    nextRound.getPandemicCards().add(lastRound.getPandemicCards().remove(random.nextInt(3)));

    // reinsert other cards into card deck
    cardDeck.getGovernmentCards().addAll(lastRound.getGovernmentCards());
    cardDeck.getPandemicCards().addAll(lastRound.getPandemicCards());

    // draw new cards
    for (int i = 0; i < 3; i++) {
      nextRound
          .getPandemicCards()
          .add(drawCard(cardDeck.getPandemicCards(), nextRound.getWorldState(), 0));
      nextRound
          .getGovernmentCards()
          .add(drawCard(cardDeck.getGovernmentCards(), nextRound.getWorldState(), 0));
    }
    this.rounds.add(nextRound);

    if (!isGameWon()) {
      this.gameState = GameState.PANDEMICS_TURN;
    }
  }

  public boolean isGameWon() {
    Round lastRound = this.rounds.get(this.getRoundNumber());
    if (lastRound.getWorldState().getPopulationMorale() <= 10) {
      this.gameState = GameState.PANDEMIC_WON;
      return true;
    }
    if (lastRound.getWorldState().getInfectedPopulation() == 0) {
      this.gameState = GameState.GOVERNMENT_WON;
      return true;
    }
    // TODO: Add other winning conditions.
    return false;
  }

  public void cancel() {
    this.gameState = GameState.CANCELED;
  }

  public void process_turn(int played_card, String player_id) throws CloneNotSupportedException {
    Round currentRound = this.rounds.get(this.getRoundNumber());

    switch (this.getGameState()) {
      case GOVERNMENTS_TURN:
        if (!this.getGovernmentPlayer().getId().equals(player_id))
          throw new UnprocessableEntryException("Not this players turn!");
        currentRound.getPlayedCards().add(currentRound.getGovernmentCards().remove(played_card));
        this.gameState = GameState.FINISHING_TURN;
        finishRound();
        break;
      case PANDEMICS_TURN:
        if (!this.getPandemicPlayer().getId().equals(player_id))
          throw new UnprocessableEntryException("Not this players turn!");
        currentRound.getPlayedCards().add(currentRound.getPandemicCards().remove(played_card));
        this.gameState = GameState.GOVERNMENTS_TURN;
      default:
        throw new ServerErrorException("No turn possible!");
    }
  }

  public void addPandemicPlayer(Player pandemicPlayer) {
    this.pandemicPlayer = pandemicPlayer;
    this.initializeDuel();
    this.gameState = GameState.PANDEMICS_TURN;
  }

  public String getId() {
    return id;
  }

  public Player getGovernmentPlayer() {
    return governmentPlayer;
  }

  public Player getPandemicPlayer() {
    return pandemicPlayer;
  }

  public GameState getGameState() {
    return gameState;
  }

  public List<Round> getRounds() {
    return rounds;
  }

  public void addRound(Round round) {
    this.rounds.add(round);
  }
}
