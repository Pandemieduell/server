package de.pandemieduell.cards.event;

import de.pandemieduell.model.Card;
import de.pandemieduell.model.EventExecutableCard;
import de.pandemieduell.model.GameAction;
import de.pandemieduell.model.WorldState;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("NothingCard")
public class NothingExecutableCard implements EventExecutableCard {
  private int roundPlayed;

  public NothingExecutableCard() {
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
  public int getNumberOfTickets(
      final int round, final WorldState state, final List<Card> playedCards) {
    return Math.max(
        Math.round(
            100
                - (10
                    * round
                    * (float) (state.getInfectedPopulation() / state.getHealthyPopulation()))),
        1);
  }

  @Override
  public void play(final int round) {
    this.roundPlayed = round;
  }
}
