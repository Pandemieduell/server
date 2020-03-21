package de.pandemieduell.model;

public interface Action {
    boolean isApplicable(int round, WorldState state);

    void updateWorldState(MutableWorldState state);
}
