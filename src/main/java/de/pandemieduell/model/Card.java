package de.pandemieduell.model;

import java.util.List;

public interface Card {
    String getName();
    String getDescription();
    List<Action> getActions();
    int getNumberOfTickets(int round, WorldState state);
}
