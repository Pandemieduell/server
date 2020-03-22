package de.pandemieduell.transferobjects;

import de.pandemieduell.model.GameAction;

public class GameActionTransferObject {
  String description;
  String source;

  public GameActionTransferObject(GameAction gameAction) {
    this.description = gameAction.getDescription();
    this.source = gameAction.getSource();
  }
}
