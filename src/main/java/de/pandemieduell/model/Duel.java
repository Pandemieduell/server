package de.pandemieduell.model;

import java.security.SecureRandom;
import java.time.temporal.ValueRange;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

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

  public int getRoundNumber() {
    return rounds.size();
  }

  public Duel(Player governmentPlayer) {
    this.id = UUID.randomUUID().toString();
    this.rounds = new LinkedList<>();
    this.governmentPlayer = governmentPlayer;
    this.gameState = GameState.INCOMPLETE;

    this.cardDeck = new CardDeck();
    //TODO: Insert all cards to the deck.
  }

  static private <T extends Card> T drawCard(List<T> cards, WorldState state, Integer roundNumber) {
    List<Integer> ticketNumbers = cards.stream().map(a -> a.getNumberOfTickets(roundNumber, state)).collect(Collectors.toList());
    int totalTicketNumber = ticketNumbers.stream().mapToInt(Integer::intValue).sum();

    int currentBase = 0;
    Map<ValueRange, Integer> ranges = new HashMap<>();
    for(int i = 0; i < ticketNumbers.size(); ++i) {
      int currentUpper = currentBase + ticketNumbers.get(i);
      ranges.put(ValueRange.of(currentBase, currentUpper -1), i);
      currentBase = currentUpper;
    }

    int drawnTicket = new SecureRandom().nextInt(totalTicketNumber);
    int pickedCard = ranges.entrySet().stream().filter(x -> x.getKey().isValidValue(drawnTicket)).mapToInt(Map.Entry::getValue).findFirst().orElse(0);
    return cards.remove(pickedCard);
  }

  private void initializeDuel() {
    WorldState worldState = new StandardWorldState();
    Round round = new Round(0, worldState);
    //Draw 4 cards for each player
    for (int i = 0; i < 4; i++){
      round.getPandemicCards().add(drawCard(cardDeck.getPandemicCards(), worldState, 0));
      round.getGovernmentCards().add(drawCard(cardDeck.getGovernmentCards(), worldState, 0));
    }

  }

  private void finishRound() {
    //draw event card
    //TODO @david ich denke hier könnte man als nächstes weitermachen
    round.getPandemicCards().add(drawCard(cardDeck.getPandemicCards(), worldState, 0));
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
