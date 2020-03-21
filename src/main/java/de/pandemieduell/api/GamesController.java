package de.pandemieduell.api;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.core.query.Query.query;
import static de.pandemieduell.api.Authorization.getUserCredentials;

import com.mongodb.client.MongoClients;
import de.pandemieduell.api.exceptions.NotFoundException;
import de.pandemieduell.model.Duel;
import de.pandemieduell.model.GameState;
import de.pandemieduell.model.Player;
import de.pandemieduell.transferobjects.CreateUserTransferObject;
import de.pandemieduell.transferobjects.DuelStateTransferObject;
import de.pandemieduell.transferobjects.RoundTransferObject;
import java.util.List;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
public class GamesController {

  @Autowired
  MongoTemplate mongoTemplate;

  private Player findPlayer(String id) {
    Player player = mongoTemplate.findOne(query(where("id").is(id)), Player.class, "users");
    if (player == null)
      throw new NotFoundException("Player not found!");
    return player;
  }

  @PostMapping(value = "/users")
  public String registerUser(@RequestBody CreateUserTransferObject createUserObject) {
    Player p = new Player(createUserObject.name);
    mongoTemplate.save(p, "users");
    return p.getId();
  }

  @PostMapping(value = "/games")
  public String joinGame(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("random") boolean randomMatching) {
    var userCredentials = getUserCredentials(authorization);
    Player player = findPlayer(userCredentials.getId());

    if (randomMatching) {
      List<Duel> opens_duels = mongoTemplate.find(query(where("game_state").is(GameState.INCOMPLETE)), Duel.class, "duel-matchmaking-public");

      if (opens_duels.size() == 0) {
        Duel new_duel = new Duel(player);
        mongoTemplate.save(new_duel, "duel-matchmaking-public");

        return new_duel.getId();
      } else {
        Duel selected = opens_duels.get(0);
        selected.addPandemicPlayer(player);

        mongoTemplate.findAndRemove(query(where("id").is(selected.getId())), Duel.class, "duel-matchmaking-public");
        mongoTemplate.save(selected, "running-duels");

        return selected.getId();
      }
    } else {
      Duel new_duel = new Duel(player);

      mongoTemplate.save(new_duel, "duel-matchmaking-private");

      return new_duel.getId();
    }
  }

  @PostMapping(value = "/games/{gameId}")
  public String joinGame(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId) {
    var userCredentials = getUserCredentials(authorization);
    Player player = findPlayer(userCredentials.getId());

    Duel private_duel = mongoTemplate.findOne(query(where("id").is(gameId)), Duel.class, "duel-matchmaking-private");
    if (private_duel == null)
      throw new NotFoundException("Duel not found!");

    private_duel.addPandemicPlayer(player);
    mongoTemplate.findAndRemove(query(where("id").is(gameId)), Duel.class, "duel-matchmaking-private");
    mongoTemplate.save(private_duel, "running-duels");

    return private_duel.getId();
  }

  @GetMapping(value = "games/{gameId}")
  public DuelStateTransferObject getGame(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId) {
    return null; // TODO implement
  }

  @GetMapping(value = "games/{gameId}/rounds/{roundNumber}")
  public RoundTransferObject getRound(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String gameId,
      @PathVariable int roundNumber) {
    return null;
  }

  @GetMapping(value = "games/{gameId}/rounds")
  public List<RoundTransferObject> getRounds(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId) {
    return null;
  }

  @PostMapping(value = "games/{gameId}/card")
  public void playCard(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String gameId,
      @RequestParam("cardNumber") int cardNumber) {
    // TODO implement
  }

  @DeleteMapping(value = "game/{gameId}")
  public void cancelGame(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId) {
    // TODO implement
  }
}
