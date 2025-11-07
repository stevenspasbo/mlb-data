package com.spasbo.mlb.service;

import com.spasbo.mlb.entity.cassandra.GameByDate;
import com.spasbo.mlb.entity.cassandra.GameKey;
import com.spasbo.mlb.model.Game;
import com.spasbo.mlb.repository.cassandra.GameByDateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailyGameIngestionService {

  private final GameByDateRepository gameRepository;
  private final StatcastService statcastService;

  /**
   * Scheduled task to go and fetch the day's games.
   */
  @Scheduled(cron = "${application.cron.fetch-daily-games}")
  public void fetchAndSaveTodaysGames() {
    List<Game> games = this.statcastService.getTodaysGames();

    List<GameByDate> gameEntities = games.stream()
        .map(this::mapDtoToEntity)
        .toList();

    if (!gameEntities.isEmpty()) {
      gameRepository.saveAll(gameEntities);
      log.info("Successfully saved {} games to Cassandra.", gameEntities.size());
    }

  }

  private GameByDate mapDtoToEntity(Game dto) {
    GameKey key = new GameKey();
    key.setOfficialDate(dto.officialDate());
    key.setGamePk(dto.gamePk());

    GameByDate entity = new GameByDate();
    entity.setKey(key);
    entity.setGameDate(dto.gameDate());
    entity.setAbstractGameState(dto.status().abstractGameState());
    entity.setDetailedState(dto.status().detailedState());
    entity.setVenueId(dto.venue().id());
    entity.setVenueName(dto.venue().name());

    entity.setHomeTeamId(dto.teams().home().getId());
    entity.setHomeTeamName(dto.teams().home().getName());
    entity.setAwayTeamId(dto.teams().away().getId());
    entity.setAwayTeamName(dto.teams().away().getName());

    return entity;
  }
}
