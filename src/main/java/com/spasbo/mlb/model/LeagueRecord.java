package com.spasbo.mlb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeagueRecord(int wins, int loses, String pct) {}
