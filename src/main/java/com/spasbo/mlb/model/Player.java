package com.spasbo.mlb.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player implements Serializable {
  private long id;
  private String fullName;
  private String link;
  private String firstName;
  private String lastName;
  private Position primaryPosition;
}
