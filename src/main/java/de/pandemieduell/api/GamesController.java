package de.pandemieduell.api;

import de.pandemieduell.model.Duel;
import org.springframework.web.bind.annotation.*;

@RestController
public class GamesController {

  @PostMapping(value = "/users", consumes = "text/plain")
  public String registerUser(@RequestBody String username) {
    return null; // TODO implement
  }

  @PostMapping(value = "/games")
  public String joinGame(@RequestParam("playerId") String playerId, @RequestParam("random") boolean randomMatching) {
    return null; // TODO implement
  }

  @PostMapping(value = "/games/{gameId}")
  public String joinGame(@RequestParam("playerId") String playerId, @PathVariable String gameId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}")
  public Duel getGame(@PathVariable long gameId) {
    return null; // TODO implement
  }

  @PostMapping(value = "games/{gameId}/card")
  public void playCard(
      @PathVariable String gameId,
      @RequestParam("playerId") String playerId,
      @RequestParam("cardNumber") int cardNumber) {
    // TODO implement
  }

  @DeleteMapping(value = "game/{gameId}")
  public void cancelGame(@PathVariable String gameId) {
    // TODO implement
  }
}
