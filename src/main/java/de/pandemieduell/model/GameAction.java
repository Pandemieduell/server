package de.pandemieduell.model;

public interface GameAction {
  boolean isApplicable(int round, WorldState state);

  void updateWorldState(MutableWorldState state);
}
