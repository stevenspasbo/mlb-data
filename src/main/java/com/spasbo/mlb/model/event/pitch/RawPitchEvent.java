package com.spasbo.mlb.model.event.pitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models a pitch event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawPitchEvent extends PitchEvent {
  private long pitcherId;
  private long batterId;
}
