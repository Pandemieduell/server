package de.pandemieduell.transferobjects;

import de.pandemieduell.model.Duel;
import de.pandemieduell.model.GameState;

public class DuelStateTransferObject {
  GameState duelState;
  int currentRoundNumber;

  public DuelStateTransferObject(Duel duel) {
    this.duelState = duel.getGameState();
    this.currentRoundNumber = duel.getRoundNumber();
  }
}
