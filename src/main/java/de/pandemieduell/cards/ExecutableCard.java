package de.pandemieduell.cards;

import de.pandemieduell.model.Card;
import de.pandemieduell.model.GameAction;
import de.pandemieduell.model.WorldState;
import java.util.List;

public interface ExecutableCard {
  String getName();

  String getDescription();

  List<GameAction> getGameActions();

  int getNumberOfTickets(int round, WorldState state, List<Card> playedCards);

  void play(int round);
}
