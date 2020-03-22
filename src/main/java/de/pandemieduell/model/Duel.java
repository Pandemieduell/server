package de.pandemieduell.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
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
  }

  public void addPandemicPlayer(Player pandemicPlayer) {
    this.pandemicPlayer = pandemicPlayer;
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
