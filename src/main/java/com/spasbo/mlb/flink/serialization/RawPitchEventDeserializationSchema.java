package com.spasbo.mlb.flink.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spasbo.mlb.model.event.pitch.RawPitchEvent;
import java.io.IOException;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

/**
 * Flink deserializer for PitchEvent.
 */
public class RawPitchEventDeserializationSchema implements DeserializationSchema<RawPitchEvent> {

  private transient ObjectMapper objectMapper;

  @Override
  public RawPitchEvent deserialize(byte[] bytes) throws IOException {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    return objectMapper.readValue(bytes, RawPitchEvent.class);
  }

  @Override
  public boolean isEndOfStream(RawPitchEvent event) {
    return false;
  }

  @Override
  public TypeInformation<RawPitchEvent> getProducedType() {
    return TypeInformation.of(RawPitchEvent.class);
  }
}
