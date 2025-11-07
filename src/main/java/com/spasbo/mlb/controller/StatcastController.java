package com.spasbo.mlb.controller;

import com.spasbo.mlb.model.Game;
import com.spasbo.mlb.service.StatcastService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RestController
public class StatcastController {

  private final StatcastService statcastService;

  @GetMapping("/games")
  public ResponseEntity<List<Game>> getGames() {
    return ResponseEntity.ok(this.statcastService.getTodaysGames());
  }

  @GetMapping("/game/{gameId}/stream")
  public SseEmitter streamGame(@PathVariable int gameId) {
    return this.statcastService.registerEmitter(gameId,
        new SseEmitter(TimeUnit.MINUTES.toMillis(30)));
  }

}
