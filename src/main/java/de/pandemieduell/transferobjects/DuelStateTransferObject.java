package de.pandemieduell.transferobjects;

import de.pandemieduell.model.Duel;
import de.pandemieduell.model.GameState;
import de.pandemieduell.model.Player;

import java.util.List;
import java.util.stream.Collectors;

public class DuelStateTransferObject {
  String id;
  GameState duelState;
  int currentRoundNumber;
  PlayerTransferObject governmentPlayer;
  PlayerTransferObject pandemicPlayer;
  List<RoundTransferObject> rounds;


  public DuelStateTransferObject(Duel duel) {
    this.id = duel.getId();
    this.duelState = duel.getGameState();
    this.currentRoundNumber = duel.getRoundNumber();
    this.governmentPlayer = new PlayerTransferObject(duel.getGovernmentPlayer().getUsername(), duel.getGovernmentPlayer().getId(), null);
    this.pandemicPlayer = new PlayerTransferObject(duel.getPandemicPlayer().getUsername(), duel.getPandemicPlayer().getId(), null);
    this.rounds = duel.getRounds().stream().map(RoundTransferObject::new).collect(Collectors.toList());
  }
}
