package com.spasbo.mlb.model.event.pitch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Models a pitch event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class RawPitchEvent extends PitchEvent {
  private long pitcherId;
  private long batterId;
}
