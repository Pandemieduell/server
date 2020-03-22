package de.pandemieduell.api;

import static de.pandemieduell.api.Authorization.getPlayerCredentials;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import de.pandemieduell.api.exceptions.NotFoundException;
import de.pandemieduell.api.exceptions.UnauthorizedException;
import de.pandemieduell.api.exceptions.UnprocessableEntryException;
import de.pandemieduell.model.Duel;
import de.pandemieduell.model.GameState;
import de.pandemieduell.model.Player;
import de.pandemieduell.model.Round;
import de.pandemieduell.transferobjects.*;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class GamesController {

  @Autowired MongoTemplate mongoTemplate;

  private Player findAndAuthorizePlayer(String authorization) {
    PlayerCredentials credentials = getPlayerCredentials(authorization);
    Player player =
        mongoTemplate.findOne(query(where("id").is(credentials.getId())), Player.class, "players");
    if (player == null) throw new UnauthorizedException("Unknown Credentials!");
    String token =
        mongoTemplate
            .findOne(
                query(where("id").is(credentials.getId())), PlayerCredentials.class, "credentials")
            .getToken();
    if (!token.equals(credentials.getToken()))
      throw new UnauthorizedException("Unknown Credentials!");
    return player;
  }

  private Duel findRunningGame(String gameId, Player player) {
    Duel duel = mongoTemplate.findOne(query(where("id").is(gameId)), Duel.class, "runningDuels");
    if (duel == null) throw new NotFoundException("Game not found!");

    // check that the player is part of the duel
    if (!(player.getId().equals(duel.getGovernmentPlayer().getId())
        || player.getId().equals(duel.getPandemicPlayer().getId())))
      throw new UnauthorizedException("Player is not part of this game!");
    return duel;
  }

  @PostMapping(value = "/players")
  public PlayerTransferObject registerPlayer(
      @RequestBody CreatePlayerTransferObject createPlayerObject) {
    Player p = new Player(createPlayerObject.name);
    mongoTemplate.save(p, "players");
    mongoTemplate.save(new PlayerCredentials(p.getId(), createPlayerObject.token), "credentials");
    return new PlayerTransferObject(createPlayerObject.name, p.getId(), createPlayerObject.token);
  }

  @PostMapping(value = "/games")
  public MatchmakingTransferObject joinGame(
      @RequestHeader("Authorization") String authorization,
      @RequestParam("random") boolean randomMatching)
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    Player player = findAndAuthorizePlayer(authorization);

    if (randomMatching) {
      List<Duel> opens_duels =
          mongoTemplate.find(
              query(where("game_state").is(GameState.INCOMPLETE)),
              Duel.class,
              "duelMatchmakingPublic");

      for (Duel selected : opens_duels) {
        if (selected.getGovernmentPlayer().getId().equals(player.getId())) {
          continue;
        } else {
          selected.addPandemicPlayer(player);

          mongoTemplate.findAndRemove(
              query(where("id").is(selected.getId())), Duel.class, "duelMatchmakingPublic");
          mongoTemplate.save(selected, "runningDuels");

          return new MatchmakingTransferObject(selected.getId());
        }
      }

      Duel new_duel = new Duel(player);
      mongoTemplate.save(new_duel, "duelMatchmakingPublic");

      return new MatchmakingTransferObject(new_duel.getId());
    } else {
      Duel new_duel = new Duel(player);

      mongoTemplate.save(new_duel, "duelMatchmakingPrivate");

      return new MatchmakingTransferObject(new_duel.getId());
    }
  }

  @PostMapping(value = "/games/{gameId}")
  public MatchmakingTransferObject joinGame(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId)
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    Player player = findAndAuthorizePlayer(authorization);

    Duel private_duel =
        mongoTemplate.findOne(query(where("id").is(gameId)), Duel.class, "duelMatchmakingPrivate");
    if (private_duel == null) throw new NotFoundException("Duel not found!");

    if (private_duel.getGovernmentPlayer().getId().equals(player.getId()))
      throw new UnprocessableEntryException("Player is not allowed play against himself!");

    private_duel.addPandemicPlayer(player);
    mongoTemplate.findAndRemove(
        query(where("id").is(gameId)), Duel.class, "duelMatchmakingPrivate");
    mongoTemplate.save(private_duel, "runningDuels");

    return new MatchmakingTransferObject(private_duel.getId());
  }

  @GetMapping(value = "games/{gameId}")
  public DuelStateTransferObject getGame(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException,
          InvocationTargetException, ClassNotFoundException {
    Player player = findAndAuthorizePlayer(authorization);
    Duel duel = findRunningGame(gameId, player);

    return new DuelStateTransferObject(duel);
  }

  @GetMapping(value = "games/{gameId}/rounds/{roundNumber}")
  public RoundTransferObject getRound(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String gameId,
      @PathVariable int roundNumber)
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    Player player = findAndAuthorizePlayer(authorization);
    Duel duel = findRunningGame(gameId, player);

    Round round = duel.getRounds().get(roundNumber);
    if (round == null) {
      throw new NotFoundException("Round not found!");
    }

    return new RoundTransferObject(round);
  }

  @GetMapping(value = "games/{gameId}/rounds")
  public List<RoundTransferObject> getRounds(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId)
      throws InvocationTargetException, NoSuchMethodException, InstantiationException,
          IllegalAccessException, ClassNotFoundException {
    Player player = findAndAuthorizePlayer(authorization);
    Duel duel = findRunningGame(gameId, player);

    List<RoundTransferObject> transferObjects = new LinkedList<>();
    for (Round round : duel.getRounds()) {
      transferObjects.add(new RoundTransferObject(round));
    }

    return transferObjects;
  }

  @PostMapping(value = "games/{gameId}/card")
  public void playCard(
      @RequestHeader("Authorization") String authorization,
      @PathVariable String gameId,
      @RequestParam("cardNumber") int cardNumber)
      throws CloneNotSupportedException, InvocationTargetException, NoSuchMethodException,
          InstantiationException, IllegalAccessException, ClassNotFoundException {
    Player player = findAndAuthorizePlayer(authorization);
    Duel duel = findRunningGame(gameId, player);

    duel.process_turn(cardNumber, player.getId());
    mongoTemplate.save(duel, "runningDuels");
  }

  @DeleteMapping(value = "games/{gameId}")
  public void cancelGame(
      @RequestHeader("Authorization") String authorization, @PathVariable String gameId) {
    Player player = findAndAuthorizePlayer(authorization);
    Duel duel = findRunningGame(gameId, player);

    duel.cancel();

    mongoTemplate.findAndRemove(query(where("id").is(gameId)), Duel.class, "runningDuels");
    mongoTemplate.save(duel, "finishedDuels");
  }
}
