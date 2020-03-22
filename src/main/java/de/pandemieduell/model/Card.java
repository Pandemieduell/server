package de.pandemieduell.model;

import java.util.List;

public interface Card {
  String getName();

  String getDescription();

  List<GameAction> getGameActions();

  int getNumberOfTickets(int round, WorldState state);
}
