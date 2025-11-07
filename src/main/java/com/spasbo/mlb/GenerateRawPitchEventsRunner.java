package com.spasbo.mlb;

import com.spasbo.mlb.model.Player;
import com.spasbo.mlb.model.event.pitch.PitchEvent;
import com.spasbo.mlb.model.event.pitch.RawPitchEvent;
import com.spasbo.mlb.service.GameIngestionService;
import com.spasbo.mlb.service.PlayerService;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Command line runner to push pitch events to Kafka.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class GenerateRawPitchEventsRunner implements CommandLineRunner {

  private final PlayerService playerService;
  private final KafkaTemplate<String, PitchEvent> kafkaTemplate;
  private final GameIngestionService gameIngestionService;

  @Value("${application.pitch-event-topic}")
  private String pitchEventTopic;

  /**
   * {@inheritDoc}
   */
  public void run(String... args) {
    gameIngestionService.fetchAndSaveTodaysGames();;

    log.info("Generating events for Kafka...");
    Random random = new Random();
    // 660271 -> Ohtani
    // 675911 -> Strider
    // 453286 -> Scherzer
    // 808963 -> Sasaki
    List<Player> allPlayers = playerService.getPlayers().stream().toList();
    List<Integer> playerIds = List.of(660271, 675911, 453286, 808963);

    for (int i = 0; i < 100; i++) {
      RawPitchEvent event = new RawPitchEvent();

      event.setPitchId(UUID.randomUUID().toString());
      event.setVelocity(85 + random.nextDouble() * 15);
      event.setSpinRate(2000 + random.nextDouble() * 500);
      event.setTimestamp(Instant.now().toString());
      event.setPitcherId(playerIds.get(random.nextInt(playerIds.size())));
      event.setBatterId(allPlayers.get(random.nextInt(allPlayers.size())).getId());

      kafkaTemplate.send(pitchEventTopic, event.getPitchId(), event);
    }
  }
}
