package de.pandemieduell.cards.pandemic;

import de.pandemieduell.model.*;
import java.util.LinkedList;
import java.util.List;

public class SpreadViaAnimalsCard implements PandemicCard {
  private int roundPlayed;

  public SpreadViaAnimalsCard() {
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

    int round_played = this.roundPlayed;

    action.add(
        new GameAction() {

          @Override
          public boolean isApplicable(int round, WorldState state) {
            return round_played - round == 0;
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
        });

    action.add(
        new GameAction() {

          @Override
          public boolean isApplicable(int round, WorldState state) {
            return round - round_played == 4;
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
