package de.pandemieduell.api;


import de.pandemieduell.model.GameState;
import de.pandemieduell.model.Round;
import org.springframework.web.bind.annotation.*;

@RestController
public class GamesController {

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST, consumes = "text/plain")
    public long registerUser(@RequestBody String username) {
        return 0; //TODO implement
    }

    @RequestMapping(value = "/joinGame", method = RequestMethod.GET)
    public long joinGame(@RequestParam("playerId") long playerId) {
        return 0; //TODO implement
    }

    @RequestMapping(value = "games/{gameId}/currentRound", method = RequestMethod.GET)
    public Round getCurrentRound(@PathVariable long gameId) {
        return null; //TODO implement
    }

    @RequestMapping(value = "games/{gameId}/round/{roundId}", method = RequestMethod.GET)
    public Round getRound(@PathVariable long gameId, @PathVariable long roundId) {
        return null; //TODO implement
    }

    @RequestMapping(value = "games/{gameId}/state", method = RequestMethod.GET)
    public GameState getCurrentGameState(@PathVariable long gameId) {
        return null; //TODO implement
    }

    @RequestMapping(value = "games/{gameId}/card")
    public void playCard(@PathVariable long gameId, @RequestParam("playerId") long playerId,
                         @RequestParam("cardNumber") int cardNumber) {
        //TODO implement
    }

    @RequestMapping(value = "game/{gameId}/cancel", method = RequestMethod.DELETE)
    public void cancelGame(@PathVariable long gameId) {
        //TODO implement
    }
}
