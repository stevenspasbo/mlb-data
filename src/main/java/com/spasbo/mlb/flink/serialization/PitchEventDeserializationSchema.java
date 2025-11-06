package com.spasbo.mlb.flink.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spasbo.mlb.model.event.PitchEvent;
import java.io.IOException;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

/**
 * Flink deserializer for PitchEvent.
 */
public class PitchEventDeserializationSchema implements DeserializationSchema<PitchEvent> {

  private transient ObjectMapper objectMapper;

  @Override
  public PitchEvent deserialize(byte[] bytes) throws IOException {
    if (objectMapper == null) {
      objectMapper = new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    return objectMapper.readValue(bytes, PitchEvent.class);
  }

  @Override
  public boolean isEndOfStream(PitchEvent event) {
    return false;
  }

  @Override
  public TypeInformation<PitchEvent> getProducedType() {
    return TypeInformation.of(PitchEvent.class);
  }
}
