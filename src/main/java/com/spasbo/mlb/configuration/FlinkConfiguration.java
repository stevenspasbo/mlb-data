package com.spasbo.mlb.configuration;

import com.spasbo.mlb.flink.serialization.RawPitchEventDeserializationSchema;
import com.spasbo.mlb.model.event.pitch.CleanPitchEvent;
import com.spasbo.mlb.model.event.pitch.RawPitchEvent;
import lombok.RequiredArgsConstructor;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.json.JsonSerializationSchema;
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
        .setParallelism(1);
  }

  @Bean
  public KafkaSource<RawPitchEvent> rawPitchEventSource() {
    return KafkaSource.<RawPitchEvent>builder()
        .setBootstrapServers(kafkaBootstrapAddress)
        .setTopics(pitchEventTopic)
        .setGroupId("pitch-group")
        .setStartingOffsets(OffsetsInitializer.earliest())
        .setValueOnlyDeserializer(new RawPitchEventDeserializationSchema())
        .build();
  }

  @Bean
  public KafkaSink<CleanPitchEvent> cleanPitchEventKafkaSink() {
    return KafkaSink.<CleanPitchEvent>builder()
        .setBootstrapServers(kafkaBootstrapAddress)
        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
            .setTopic("clean-pitches")
            .setValueSerializationSchema(new JsonSerializationSchema<CleanPitchEvent>())
            .build())
        .build();
  }

}
