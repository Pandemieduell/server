package de.pandemieduell.model;

import java.util.LinkedList;
import java.util.List;

public class CardDeck {
  private List<Card> governmentCards;
  private List<Card> pandemicCards;
  private List<Card> eventCards;

  public CardDeck() {
    this.governmentCards = new LinkedList<>();
    this.pandemicCards = new LinkedList<>();
    this.eventCards = new LinkedList<>();
  }

  public List<Card> getGovernmentCards() {
    return governmentCards;
  }

  public List<Card> getPandemicCards() {
    return pandemicCards;
  }

  public List<Card> getEventCards() {
    return eventCards;
  }

  public void insertGovernmentCard(Card card) {
    this.governmentCards.add(card);
  }

  public void insertPandemicCard(Card card) {
    this.pandemicCards.add(card);
  }

  public void insertEventCard(Card card) {
    this.eventCards.add(card);
  }
}
