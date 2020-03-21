package de.pandemieduell.model;

import java.util.List;

public class Duel {
    private long id;
    private long governmentPlayerID;
    private long pandemicPlayerID;
    private GameState gameState;
    private List<Round> rounds;

    public int getRoundNumber(){
        return rounds.size();
    }
}
