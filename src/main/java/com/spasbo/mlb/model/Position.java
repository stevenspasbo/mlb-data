package com.spasbo.mlb.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
  private String code;
  private String name;
  private String abbreviation;
  private Position primaryPosition;
}
