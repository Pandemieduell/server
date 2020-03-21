package de.pandemieduell.model;

import java.util.LinkedList;
import java.util.List;

public class Round {
  private int roundNumber;
  private List<GovernmentCard> governmentCards;
  private List<PandemicCard> pandemicCards;
  private WorldState state;
  private List<GameAction> executedActions;

  public Round(int roundNumber, WorldState state) {
    this.roundNumber = roundNumber;
    this.state = state;
    this.executedActions = new LinkedList<>();
  }

  public int getRoundNumber() {
    return roundNumber;
  }

  public List<GovernmentCard> getGovernmentCards() {
    return governmentCards;
  }

  public List<PandemicCard> getPandemicCards() {
    return pandemicCards;
  }

  public WorldState getState() {
    return state;
  }

  public List<GameAction> getExecutedActions() {
    return executedActions;
  }
}
