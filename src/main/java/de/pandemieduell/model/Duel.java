package de.pandemieduell.model;

import de.pandemieduell.api.exceptions.UnprocessableEntryException;
import de.pandemieduell.cards.ExecutableCard;
import de.pandemieduell.cards.event.NothingExecutableCard;
import de.pandemieduell.cards.government.InvestIntoResearchExecutableCard;
import de.pandemieduell.cards.pandemic.SpreadViaAnimalsExecutableCard;
import java.lang.reflect.InvocationTargetException;
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
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 0));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 1));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 2));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 3));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 4));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 5));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 6));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 7));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 8));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 9));
    this.cardDeck.insertEventCard(new Card(NothingExecutableCard.class.getCanonicalName(), 10));

    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 11));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 12));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 13));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 14));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 15));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 16));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 17));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 18));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 19));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 20));
    this.cardDeck.insertGovernmentCard(
        new Card(InvestIntoResearchExecutableCard.class.getCanonicalName(), 21));

    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 22));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 23));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 24));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 25));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 26));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 27));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 28));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 29));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 30));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 31));
    this.cardDeck.insertPandemicCard(
        new Card(SpreadViaAnimalsExecutableCard.class.getCanonicalName(), 32));
  }

  private static Card drawCard(
      List<Card> cards, WorldState state, List<Card> playedCards, Integer roundNumber)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException, ClassNotFoundException {

    List<ExecutableCard> executableCards = new LinkedList<>();
    for (Card card : cards) {
      executableCards.add(card.getCardClass().getDeclaredConstructor().newInstance());
    }

    List<Integer> ticketNumbers =
        executableCards
            .stream()
            .map(a -> a.getNumberOfTickets(roundNumber, state, playedCards))
            .collect(Collectors.toList());
    int totalTicketNumber = ticketNumbers.stream().mapToInt(Integer::intValue).sum();

    int currentBase = 0;
    Map<ValueRange, Integer> ranges = new HashMap<>();
    for (int i = 0; i < ticketNumbers.size(); ++i) {
      int currentUpper = currentBase + ticketNumbers.get(i);
      ranges.put(ValueRange.of(currentBase, currentUpper - 1), i);
      currentBase = currentUpper;
    }

    if (totalTicketNumber <= 0) throw new UnprocessableEntryException("Card deck is empty!");

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

  private void initializeDuel()
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    StandardWorldState worldState = new StandardWorldState();
    Round round = new Round(0, worldState);
    // Draw 4 cards for each player
    for (int i = 0; i < 4; i++) {

      round
          .getPandemicCards()
          .add(drawCard(this.cardDeck.getPandemicCards(), worldState, this.getAllPlayedCards(), 0));

      round
          .getGovernmentCards()
          .add(
              drawCard(
                  this.cardDeck.getGovernmentCards(), worldState, this.getAllPlayedCards(), 0));
    }
    this.rounds.add(round);
  }

  private List<Card> getAllPlayedCards() {
    return this.rounds
        .stream()
        .map(Round::getPlayedCards)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  // to be called when both players made their turn and the game can advance to the next round
  private void finishRound()
      throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException,
          InstantiationException, IllegalAccessException, ClassNotFoundException {
    Round lastRound = this.rounds.get(this.getRoundNumber());
    // draw event card and add to played cards

    Card event_card =
        drawCard(
            this.cardDeck.getEventCards(),
            lastRound.getWorldState(),
            this.getAllPlayedCards(),
            lastRound.getRoundNumber());
    event_card.play(this.getRoundNumber());
    lastRound.getPlayedCards().add(event_card);

    // TOFO: Simulate
    // find applicable actions
    List<Card> cards =
        this.rounds
            .parallelStream()
            .map(Round::getPlayedCards)
            .flatMap(List::stream)
            .collect(Collectors.toList());
    List<ExecutableCard> executableCards = new LinkedList<>();
    for (Card card : cards) {
      executableCards.add(card.getCardClass().getDeclaredConstructor().newInstance());
    }

    List<GameAction> actionsToExecute =
        executableCards
            .stream()
            .map(ExecutableCard::getGameActions)
            .flatMap(List::stream)
            .filter(x -> x.isApplicable(this.getRoundNumber(), lastRound.getWorldState()))
            .collect(Collectors.toList());

    // execute applicable actions
    StandardWorldState action_state = (StandardWorldState) lastRound.getWorldState().clone();
    for (GameAction action : actionsToExecute) {
      action.updateWorldState(action_state);
    }

    action_state.infectPeople(
        Math.round(action_state.getInfectedPopulation() * action_state.getInfectionRate()));
    action_state.killPeople(
        Math.round(action_state.getDeadPopulation() * action_state.getCaseFatalityRate()));

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
          .add(
              drawCard(
                  cardDeck.getPandemicCards(),
                  nextRound.getWorldState(),
                  this.getAllPlayedCards(),
                  0));
      nextRound
          .getGovernmentCards()
          .add(
              drawCard(
                  cardDeck.getGovernmentCards(),
                  nextRound.getWorldState(),
                  this.getAllPlayedCards(),
                  0));
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

  public void process_turn(int played_card, String player_id)
      throws CloneNotSupportedException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, InvocationTargetException, ClassNotFoundException {
    Round currentRound = this.rounds.get(this.getRoundNumber());

    switch (this.getGameState()) {
      case GOVERNMENTS_TURN:
        if (!this.getGovernmentPlayer().getId().equals(player_id))
          throw new UnprocessableEntryException("Not this players turn!");

        Card played_gov_card = currentRound.getGovernmentCards().remove(played_card);
        played_gov_card.play(this.getRoundNumber());

        currentRound.getPlayedCards().add(played_gov_card);
        this.gameState = GameState.FINISHING_TURN;
        finishRound();
        break;
      case PANDEMICS_TURN:
        if (!this.getPandemicPlayer().getId().equals(player_id))
          throw new UnprocessableEntryException("Not this players turn!");

        Card played_pan_card = currentRound.getPandemicCards().remove(played_card);
        played_pan_card.play(this.getRoundNumber());

        currentRound.getPlayedCards().add(played_pan_card);
        this.gameState = GameState.GOVERNMENTS_TURN;
        break;
      default:
        throw new ServerErrorException("No turn possible!");
    }
  }

  public void addPandemicPlayer(Player pandemicPlayer)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException,
          InvocationTargetException, ClassNotFoundException {
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
