package de.pandemieduell.api;

import de.pandemieduell.model.GameState;
import de.pandemieduell.model.Round;
import org.springframework.web.bind.annotation.*;

@RestController
public class GamesController {

  @PostMapping(value = "/users", consumes = "text/plain")
  public String registerUser(@RequestBody String username) {
    return null; // TODO implement
  }

  @PostMapping(value = "/games")
  public String joinGame(@RequestParam("playerId") String playerId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}/currentRound")
  public Round getCurrentRound(@PathVariable long gameId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}/round/{roundId}")
  public Round getRound(@PathVariable long gameId, @PathVariable long roundId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}/state")
  public GameState getCurrentGameState(@PathVariable long gameId) {
    return null; // TODO implement
  }

  @PostMapping(value = "games/{gameId}/card")
  public void playCard(
      @PathVariable String gameId,
      @RequestParam("playerId") String playerId,
      @RequestParam("cardNumber") int cardNumber) {
    // TODO implement
  }

  @PostMapping(value = "game/{gameId}/cancel")
  public void cancelGame(@PathVariable String gameId) {
    // TODO implement
  }
}
