package com.spasbo.mlb.service;

import com.spasbo.mlb.model.Game;
import com.spasbo.mlb.model.ScheduleResponse;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class StatcastService {

  private static final URI BASE_URI = URI.create("https://statsapi.mlb.com");

  private final RestTemplate restTemplate;

  @Value("${application.sport-id}")
  private Integer sportId;

  public StatcastService() {
    this.restTemplate = new RestTemplate();
  }

  public List<Game> getTodaysGames() {
    URI scheduleUri = BASE_URI.resolve("/api/v1/schedule?sportId=%s".formatted(sportId));

    ResponseEntity<ScheduleResponse> response = restTemplate.getForEntity(scheduleUri, ScheduleResponse.class);
    if (response.getStatusCode().is2xxSuccessful()) {
      ScheduleResponse scheduleResponse = response.getBody();
      if (scheduleResponse != null && !scheduleResponse.dates().isEmpty()) {
        List<Game> games = scheduleResponse.dates().get(0).games();
        log.info("Retrieved {} games on {}", games.size(), LocalDate.now());

        return games;
      }
    }

    return List.of();
  }

}
