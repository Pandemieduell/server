package de.pandemieduell.transferobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.pandemieduell.cards.ExecutableCard;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CardTransferObject {
  String name;
  String description;

  public CardTransferObject(ExecutableCard card) {
    this.name = card.getName();
    this.description = card.getDescription();
  }
}
