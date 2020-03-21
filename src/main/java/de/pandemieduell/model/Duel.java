package de.pandemieduell.model;

import java.util.List;

public class Duel {
  private String id;
  private Player governmentPlayer;
  private Player pandemicPlayer;
  private GameState gameState;
  private List<Round> rounds;

  public int getRoundNumber() {
    return rounds.size();
  }
}
