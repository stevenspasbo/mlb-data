package com.spasbo.mlb;

import com.spasbo.mlb.model.event.PitchEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FlinkJobRunner implements CommandLineRunner {

  private final StreamExecutionEnvironment env;
  private final KafkaSource<PitchEvent> source;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run(String... args) throws Exception {
    log.info("Starting Flink job runner...");
    env.fromSource(source, WatermarkStrategy.noWatermarks(), "pitch-source")
        .map(event -> event)
        .print();

    env.execute("Pitch Event Stream Processing");
  }
}
