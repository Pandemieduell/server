package de.pandemieduell.cards.government;

import de.pandemieduell.model.*;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("InvestIntoResearchCard")
public class InvestIntoResearchExecutableCard implements GovernmentExecutableCard {
  private int roundPlayed;

  public InvestIntoResearchExecutableCard() {
    this.roundPlayed = 0;
  }

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

    action.add(new InitialAction(this.roundPlayed));

    action.add(new SecondaryAction(this.roundPlayed));

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

  static class InitialAction implements GameAction {

    int roundPlayed;

    public InitialAction(final int roundPlayed) {
      this.roundPlayed = roundPlayed;
    }

    @Override
    public boolean isApplicable(int round, WorldState state) {
      return this.roundPlayed - round == 0;
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
  }

  static class SecondaryAction implements GameAction {
    int roundPlayed;

    public SecondaryAction(final int roundPlayed) {
      this.roundPlayed = roundPlayed;
    }

    @Override
    public boolean isApplicable(int round, WorldState state) {
      return round - this.roundPlayed == 2 || round - this.roundPlayed == 4;
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
  }
}
