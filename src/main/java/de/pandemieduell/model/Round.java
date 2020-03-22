package de.pandemieduell.model;

import java.util.LinkedList;
import java.util.List;

public class Round {
  private int roundNumber;
  private LinkedList<Card> governmentCards;
  private LinkedList<Card> pandemicCards;
  private StandardWorldState worldState;
  private LinkedList<Card> playedCards;

  public Round(int roundNumber, StandardWorldState worldState) {
    this.roundNumber = roundNumber;
    this.worldState = worldState;
    this.governmentCards = new LinkedList<>();
    this.playedCards = new LinkedList<>();
    this.pandemicCards = new LinkedList<>();
  }

  public int getRoundNumber() {
    return roundNumber;
  }

  public List<Card> getGovernmentCards() {
    return governmentCards;
  }

  public List<Card> getPandemicCards() {
    return pandemicCards;
  }

  public StandardWorldState getWorldState() {
    return worldState;
  }

  public List<Card> getPlayedCards() {
    return playedCards;
  }
}
