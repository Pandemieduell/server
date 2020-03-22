package de.pandemieduell.cards.event;

import de.pandemieduell.model.Card;
import de.pandemieduell.model.EventCard;
import de.pandemieduell.model.GameAction;
import de.pandemieduell.model.WorldState;

import java.util.LinkedList;
import java.util.List;

public class NothingCard implements EventCard {
    private int roundPlayed;

    public NothingCard() {
        this.roundPlayed = 0;
    }

    @Override
    public String getName() {
        return "Nichts passiert";
    }

    @Override
    public String getDescription() {
        return "Es passiert nichts besonderes.";
    }

    @Override
    public List<GameAction> getGameActions() {
        return new LinkedList<>();
    }

    @Override
    public int getNumberOfTickets(final int round, final WorldState state, final List<Card> playedCards) {
        return Math.round(100-(this.roundPlayed * (float) (state.getInfectedPopulation() / state.getHealthyPopulation())));
    }

    @Override
    public void play(final int round) {
        this.roundPlayed = round;
    }
}
