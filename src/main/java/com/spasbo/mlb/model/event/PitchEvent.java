package com.spasbo.mlb.model.event;

import java.io.Serializable;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models a pitch event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PitchEvent implements Serializable {

  private String pitchId;

  private String player;

  private double velocity;

  private double spinRate;

  private Instant timestamp;

}
