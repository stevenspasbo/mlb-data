package com.spasbo.mlb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Game(int gamePk, String link, Teams teams, Instant gameDate) {}
