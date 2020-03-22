package de.pandemieduell.transferobjects;

import de.pandemieduell.model.Card;
import de.pandemieduell.model.GameAction;
import de.pandemieduell.model.Round;
import de.pandemieduell.model.WorldState;
import java.util.LinkedList;
import java.util.List;

public class RoundTransferObject {
  int roundNumber;
  List<CardTransferObject> availableGovernmentCards;
  List<CardTransferObject> availablePandemicCards;
  List<GameActionTransferObject> executedGameActions;
  WorldState worldState;
  List<CardTransferObject> playedCards;

  public RoundTransferObject(Round round) {
    this.roundNumber = round.getRoundNumber();

    this.availableGovernmentCards = new LinkedList<>();
    for (Card card : round.getGovernmentCards()) {
      availableGovernmentCards.add(new CardTransferObject(card));
    }

    this.availablePandemicCards = new LinkedList<>();
    for (Card card : round.getPandemicCards()) {
      availablePandemicCards.add(new CardTransferObject(card));
    }

    this.executedGameActions = new LinkedList<>();
    for (GameAction gameAction : round.getExecutedActions()) {
      executedGameActions.add(new GameActionTransferObject(gameAction));
    }

    this.worldState = round.getWorldState();

    this.playedCards = new LinkedList<>();
    for (Card card : round.getPlayedCards()) {
      playedCards.add(new CardTransferObject(card));
    }
  }
}
