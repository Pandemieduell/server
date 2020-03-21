package de.pandemieduell.api;

import de.pandemieduell.model.Duel;
import de.pandemieduell.transferobjects.CreateUserTransferObject;
import de.pandemieduell.transferobjects.DuelStateTransferObject;
import de.pandemieduell.transferobjects.RoundTransferObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GamesController {

  @PostMapping(value = "/users")
  public String registerUser(@RequestBody CreateUserTransferObject createUserObject) {
    return null; // TODO implement
  }

  @PostMapping(value = "/games")
  public String joinGame(
      @RequestHeader("Authorization") String playerId, @RequestParam("random") boolean randomMatching) {
    return null; // TODO implement
  }

  @PostMapping(value = "/games/{gameId}")
  public String joinGame(@RequestHeader("Authorization") String playerId, @PathVariable String gameId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}")
  public DuelStateTransferObject getGame(@RequestHeader("Authorization") String playerId,
                                         @PathVariable String gameId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}/rounds/{roundId}")
  public RoundTransferObject getRound(@RequestHeader("Authorization") String playerId,
                                      @PathVariable String gameId,
                                      @PathVariable String roundId) {
    return null;
  }

  @GetMapping(value = "games/{gameId}/rounds")
  public List<RoundTransferObject> getRounds(@RequestHeader("Authorization") String playerId,
                                             @PathVariable String gameId) {
    return null;
  }

  @PostMapping(value = "games/{gameId}/card")
  public void playCard(
          @RequestHeader("Authorization") String playerId,
      @PathVariable String gameId,
      @RequestParam("cardNumber") int cardNumber) {
    // TODO implement
  }

  @DeleteMapping(value = "game/{gameId}")
  public void cancelGame(@RequestHeader("Authorization") String playerId,
                         @PathVariable String gameId) {
    // TODO implement
  }
}
