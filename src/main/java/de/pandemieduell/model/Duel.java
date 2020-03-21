package de.pandemieduell.model;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Duel {
  private String id;
  private Player governmentPlayer;
  private Player pandemicPlayer;
  private GameState gameState;
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
