package com.spasbo.mlb.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

  private int id;

  private String fullName;

  private String link;

  private String firstName;

  private String lastName;

  private Position primaryPosition;

}
