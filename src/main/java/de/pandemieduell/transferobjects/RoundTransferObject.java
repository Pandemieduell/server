package de.pandemieduell.transferobjects;

import de.pandemieduell.model.WorldState;
import java.util.List;

public class RoundTransferObject {
  int roundNumber;
  List<CardTransferObject> availableGovernmentCards;
  List<CardTransferObject> availablePandemicCards;
  List<GameActionTransferObject> executedActions;
  WorldState worldState;
}
