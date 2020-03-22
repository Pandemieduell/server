package de.pandemieduell.cards.government;

import de.pandemieduell.model.*;

import java.util.LinkedList;
import java.util.List;

public class InvestIntoResearchCard implements GovernmentCard {
    private int roundPlayed;

    @Override
    public String getName() {
        return "In Forschung investieren.";
    }

    @Override
    public String getDescription() {
        return "Das Investieren in Forschung kostet Geld, ";
    }

    @Override
    public List<GameAction> getGameActions() {
        List<GameAction> action = new LinkedList<>();

        int round_played = this.roundPlayed;


        action.add(new GameAction() {

            @Override
            public boolean isApplicable(int round, WorldState state) {
                return round_played - round == 0;
            }

            @Override
            public void updateWorldState(MutableWorldState state) {
                state.setPopulationMorale(state.getPopulationMorale() + 2);
            }

            @Override
            public String getDescription() {
                return "Vertrauen in Regierung steigt. Investitionen werden getätigt.";
            }

            @Override
            public String getSource() {
                return "Investitionen in Forschung";
            }
        });

        action.add(new GameAction() {

            @Override
            public boolean isApplicable(int round, WorldState state) {
                return round - round_played == 2 || round - round_played == 4;
            }

            @Override
            public void updateWorldState(MutableWorldState state) {
                state.setHealthSystemCapacity((long) (state.getHealthSystemCapacity() * 1.02));
            }

            @Override
            public String getDescription() {
                return "Vertrauen in Regierung steigt. Investitionen werden getätigt.";
            }

            @Override
            public String getSource() {
                return "Investitionen in Forschung";
            }
        });

        return action;
    }

    @Override
    public int getNumberOfTickets(int round, WorldState state, List<Card> playedCards) {
        return 10;
    }

    @Override
    public void play(int round) {
        this.roundPlayed = round;
    }
}
