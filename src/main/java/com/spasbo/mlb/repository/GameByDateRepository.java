package com.spasbo.mlb.repository;

import com.spasbo.mlb.entity.cassandra.GameByDate;
import com.spasbo.mlb.entity.cassandra.GameKey;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameByDateRepository extends CassandraRepository<GameByDate, GameKey> {

  List<GameByDate> findByKeyOfficialDate(LocalDate officialDate);

}
