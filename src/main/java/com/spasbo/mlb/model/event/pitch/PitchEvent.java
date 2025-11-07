package com.spasbo.mlb.model.event.pitch;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PitchEvent implements Serializable {
  protected String pitchId;
  protected double velocity;
  protected double spinRate;
  protected String timestamp;
}
