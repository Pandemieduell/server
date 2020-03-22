package de.pandemieduell.transferobjects;

import de.pandemieduell.model.Duel;
import de.pandemieduell.model.GameState;
import de.pandemieduell.model.Round;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public class DuelStateTransferObject {
  String id;
  GameState duelState;
  int currentRoundNumber;
  PlayerTransferObject governmentPlayer;
  PlayerTransferObject pandemicPlayer;
  List<RoundTransferObject> rounds;

  public DuelStateTransferObject(Duel duel)
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    this.id = duel.getId();
    this.duelState = duel.getGameState();
    this.currentRoundNumber = duel.getRoundNumber();
    this.governmentPlayer =
        new PlayerTransferObject(
            duel.getGovernmentPlayer().getUsername(), duel.getGovernmentPlayer().getId(), null);
    this.pandemicPlayer =
        new PlayerTransferObject(
            duel.getPandemicPlayer().getUsername(), duel.getPandemicPlayer().getId(), null);

    this.rounds = new LinkedList<>();
    for (Round round : duel.getRounds()) {
      this.rounds.add(new RoundTransferObject(round));
    }
  }
}
