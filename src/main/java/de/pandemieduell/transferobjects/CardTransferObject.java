package de.pandemieduell.transferobjects;

import de.pandemieduell.model.Card;

public class CardTransferObject {
  String name;
  String description;

  public CardTransferObject(Card card) {
    this.name = card.getName();
    this.description = card.getDescription();
  }
}
