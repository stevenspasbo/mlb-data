package com.spasbo.mlb.configuration;

import com.spasbo.mlb.flink.serialization.PitchEventDeserializationSchema;
import com.spasbo.mlb.model.event.PitchEvent;
import lombok.RequiredArgsConstructor;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Flink configuration.
 */
@RequiredArgsConstructor
@Configuration
public class FlinkConfiguration {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String kafkaBootstrapAddress;

  @Value("${application.pitch-event-topic}")
  private String pitchEventTopic;

  @Bean
  public StreamExecutionEnvironment streamExecutionEnvironment() throws Exception {
    return StreamExecutionEnvironment.getExecutionEnvironment()
        .setParallelism(5);
  }

  @Bean
  public KafkaSource<PitchEvent> kafkaSource() {
    return KafkaSource.<PitchEvent>builder()
        .setBootstrapServers(kafkaBootstrapAddress)
        .setTopics(pitchEventTopic)
        .setGroupId("pitch-group")
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setValueOnlyDeserializer(new PitchEventDeserializationSchema())
        .build();
  }

}
