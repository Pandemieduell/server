package de.pandemieduell.cards.pandemic;

import de.pandemieduell.model.*;
import java.util.LinkedList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Component;

@Component("SpreadViaAnimalsCard")
public class SpreadViaAnimalsExecutableCard implements PandemicExecutableCard {
  private int roundPlayed;

  public SpreadViaAnimalsExecutableCard() {
    this.roundPlayed = 0;
  }

  @Override
  public String getName() {
    return "Übertragung durch Tiere";
  }

  @Override
  public String getDescription() {
    return "Eine Übertragung des Virus durch Tiere erhöht die Ausbreitungsgeschwindigkeit des Virus.";
  }

  @Override
  public List<GameAction> getGameActions() {
    List<GameAction> action = new LinkedList<>();

    action.add(new InitialAction(0, this.roundPlayed));

    action.add(new SecondaryAction(1, this.roundPlayed));
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
    @Id int id;
    int roundPlayed;

    public InitialAction(int id, int round) {
      this.id = id;
      this.roundPlayed = round;
    }

    @Override
    public boolean isApplicable(int round, WorldState state) {
      return this.roundPlayed - round == 0;
    }

    @Override
    public void updateWorldState(MutableWorldState state) {
      state.setInfectionRate(state.getInfectionRate() + 0.05);
    }

    @Override
    public String getDescription() {
      return "Erhöht Infektionsrate.";
    }

    @Override
    public String getSource() {
      return "Übertragung durch Tiere";
    }
  }

  static class SecondaryAction implements GameAction {
    @Id int id;
    int roundPlayed;

    public SecondaryAction(int id, int round) {
      this.id = id;
      this.roundPlayed = round;
    }

    @Override
    public boolean isApplicable(int round, WorldState state) {
      return round - this.roundPlayed == 4;
    }

    @Override
    public void updateWorldState(MutableWorldState state) {
      state.setPopulationMorale(state.getPopulationMorale() - 5);
    }

    @Override
    public String getDescription() {
      return "Bevölkerungsmoral sinkt.";
    }

    @Override
    public String getSource() {
      return "Übertragung durch Tiere";
    }
  }
}
