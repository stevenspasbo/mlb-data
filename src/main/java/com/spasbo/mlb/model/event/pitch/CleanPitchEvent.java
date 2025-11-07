package com.spasbo.mlb.model.event.pitch;

import com.spasbo.mlb.model.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Models a pitch event.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleanPitchEvent extends PitchEvent {
  private Player pitcher;
  private Player batter;
}
