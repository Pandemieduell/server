package de.pandemieduell.transferobjects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.pandemieduell.model.Card;
import de.pandemieduell.model.Round;
import de.pandemieduell.model.StandardWorldState;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RoundTransferObject {
  int roundNumber;
  List<CardTransferObject> availableGovernmentCards;
  List<CardTransferObject> availablePandemicCards;
  StandardWorldState worldState;
  List<CardTransferObject> playedCards;

  public RoundTransferObject(Round round)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException, ClassNotFoundException {
    this.roundNumber = round.getRoundNumber();

    this.availableGovernmentCards = new ArrayList<>();
    for (Card card : round.getGovernmentCards()) {
      availableGovernmentCards.add(
          new CardTransferObject(card.getCardClass().getDeclaredConstructor().newInstance()));
    }

    this.availablePandemicCards = new ArrayList<>();
    for (Card card : round.getPandemicCards()) {
      availablePandemicCards.add(
          new CardTransferObject(card.getCardClass().getDeclaredConstructor().newInstance()));
    }

    this.worldState = round.getWorldState();

    this.playedCards = new ArrayList<>();
    for (Card card : round.getPlayedCards()) {
      playedCards.add(
          new CardTransferObject(card.getCardClass().getDeclaredConstructor().newInstance()));
    }
  }
}
