package com.spasbo.mlb.entity.cassandra;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@Data
@NoArgsConstructor
@PrimaryKeyClass
public class GameKey {

  @PrimaryKeyColumn(name = "official_date", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
  private LocalDate officialDate;

  @PrimaryKeyColumn(name = "game_pk", ordinal = 1, type = PrimaryKeyType.CLUSTERED, ordering = Ordering.ASCENDING)
  private int gamePk;

}
