package com.spasbo.mlb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Games(
    String date,
    int totalItems,
    int totalEvents,
    int totalGames,
    int totalGamesInProgress,
    List<Game> games,
    List<Object> events
) {}
