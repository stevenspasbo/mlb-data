package com.spasbo.mlb.entity.cassandra;

import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@Table("games_by_date")
public class GameByDate {

  @PrimaryKey
  private GameKey key;

  @Column("game_date")
  private Instant gameDate;

  @Column("abstract_game_state")
  private String abstractGameState;

  @Column("detailed_state")
  private String detailedState;

  @Column("away_team_id")
  private int awayTeamId;

  @Column("away_team_name")
  private String awayTeamName;

  @Column("home_team_id")
  private int homeTeamId;

  @Column("home_team_name")
  private String homeTeamName;

  @Column("venue_id")
  private int venueId;

  @Column("venue_name")
  private String venueName;

}
