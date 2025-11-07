package com.spasbo.mlb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spasbo.mlb.model.event.pitch.CleanPitchEvent;
import com.spasbo.mlb.model.event.pitch.RawPitchEvent;
import com.spasbo.mlb.service.StatcastService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FlinkJobRunner implements CommandLineRunner {

//  private final PlayerService playerService;
  private final StreamExecutionEnvironment env;
  private final KafkaSource<RawPitchEvent> rawPitchEventSource;
  private final KafkaSink<CleanPitchEvent> cleanPitchEventKafkaSink;
  private final ObjectMapper objectMapper;
  private final StatcastService statcastService;

  /**
   * {@inheritDoc}
   */
  @Override
  public void run(String... args) throws Exception {
    log.info("Starting Flink job runner...");

    env.fromSource(rawPitchEventSource, WatermarkStrategy.noWatermarks(), "pitch-source")
        .map(event -> {
          System.out.println("Flink Processing: " + event);
          return event;
        })
        .print();

    env.execute("Pitch Event Stream Processing");
  }

}
