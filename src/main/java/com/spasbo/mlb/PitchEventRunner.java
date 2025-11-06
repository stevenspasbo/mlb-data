package com.spasbo.mlb;

import com.spasbo.mlb.model.event.PitchEvent;
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

@RequiredArgsConstructor
@Slf4j
@Component
public class PitchEventRunner implements CommandLineRunner {

  private final KafkaTemplate<String, PitchEvent> kafkaTemplate;

  @Value("${application.pitch-event-topic}")
  private String pitchEventTopic;

  /**
   * {@inheritDoc}
   */
  public void run(String... args) {
    log.info("Generating events for Kafka...");
    Random random = new Random();
    List<String> players = List.of("Ohtani", "Cole", "Strider", "Scherzer");

    for (int i = 0; i < 100; i++) {
      PitchEvent event = new PitchEvent(
          UUID.randomUUID().toString(),
          players.get(random.nextInt(players.size())),
          85 + random.nextDouble() * 15,
          2000 + random.nextDouble() * 500,
          Instant.now()
      );

      kafkaTemplate.send(pitchEventTopic, event.getPlayer(), event);
    }
  }
}
