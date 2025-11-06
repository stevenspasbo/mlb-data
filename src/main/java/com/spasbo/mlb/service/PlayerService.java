package com.spasbo.mlb.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spasbo.mlb.model.Player;
import com.spasbo.mlb.model.Position;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class PlayerService {

  private final Map<String, Position> positionsByCode;

  private final Map<Long, Player> playersById;

  public PlayerService(ObjectMapper objectMapper) {
    positionsByCode = new HashMap<>();
    playersById = new HashMap<>();

    try {
      List<Position> positionList = objectMapper
          .readerForListOf(Position.class)
          .readValue(ResourceUtils.getFile("classpath:data/positions.json"));
      for (Position position : positionList) {
        positionsByCode.put(position.getCode(), position);
      }

      JsonNode node = objectMapper
          .readTree(ResourceUtils.getFile("classpath:data/players.json"));
      if (node.has("people")) {
        List<Player> playerList = objectMapper
            .readerForListOf(Player.class)
            .readValue(node.get("people"));
        for (Player player : playerList) {
          playersById.put(player.getId(), player);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Gets all players.
   */
  public Collection<Player> getPlayers() {
    return playersById.values();
  }

  /**
   * Gets a player by their ID.
   */
  public Player getById(long id) {
    return playersById.get(id);
  }
}
