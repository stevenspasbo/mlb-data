package com.spasbo.mlb.configuration;

import com.spasbo.mlb.model.event.pitch.PitchEvent;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

/**
 * Kafka configuration.
 */
@Slf4j
@Configuration
@EnableKafka
public class KafkaConfiguration {

  @Value(value = "${spring.kafka.bootstrap-servers}")
  private String bootstrapAddress;

  @Bean
  public KafkaAdmin kafkaAdmin() {
    return new KafkaAdmin(Map.of(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
    ));
  }

  @Bean
  public ConsumerFactory<String, PitchEvent> consumerFactory() {
    JsonDeserializer<PitchEvent> deserializer = new JsonDeserializer<>(PitchEvent.class);
    deserializer.addTrustedPackages("*");

    return new DefaultKafkaConsumerFactory<>(Map.of(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
    ), new StringDeserializer(), deserializer);
  }

  @Bean
  public ProducerFactory<String, PitchEvent> producerFactory() {
    return new DefaultKafkaProducerFactory<>(Map.of(
        CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
        ProducerConfig.LINGER_MS_CONFIG, 10,
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
    ));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PitchEvent> pitchEventListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PitchEvent> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }

  @Bean
  public KafkaTemplate<String, PitchEvent> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

  @Bean
  public NewTopic createPitchesTopic() {
    return new NewTopic("pitches", 1, (short) 1);
  }

}
