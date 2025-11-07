package com.spasbo.mlb.service;

import com.spasbo.mlb.model.Game;
import com.spasbo.mlb.model.ScheduleResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Slf4j
@Service
public class StatcastService {

  private static final URI BASE_URI = URI.create("https://statsapi.mlb.com");
  private static final URI BASE_WEBSOCKET_URI = URI.create("wss://ws.statsapi.mlb.com");

  private final RestTemplate restTemplate;
  private final HttpClient httpClient;
  private final Map<Integer, List<SseEmitter>> gameEmitters;

  @Value("${application.sport-id}")
  private Integer sportId;

  public StatcastService(RestTemplate restTemplate, HttpClient httpClient) {
    this.restTemplate = restTemplate;
    this.httpClient = httpClient;
    this.gameEmitters = new ConcurrentHashMap<>();
  }

  /**
   * Gets the list of games happening on the current day.
   */
  public List<Game> getTodaysGames() {
    URI scheduleUri = BASE_URI.resolve("/api/v1/schedule?sportId=%s".formatted(sportId));
    ResponseEntity<ScheduleResponse> response = restTemplate
        .getForEntity(scheduleUri, ScheduleResponse.class);

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

  /**
   * Registers an SSE emitter with the Statcast service.
   */
  public SseEmitter registerEmitter(int gameId, SseEmitter emitter) {
    List<SseEmitter> emitters = this.gameEmitters.computeIfAbsent(gameId, k -> {
      List<SseEmitter> list = new CopyOnWriteArrayList<>();
      connectToGame(gameId, (data) -> broadcast(gameId, data));
      return list;
    });

    emitters.add(emitter);

    Runnable cleanup = () -> {
      emitters.remove(emitter);
    };
    emitter.onCompletion(cleanup);
    emitter.onTimeout(cleanup);
    emitter.onError(e -> {
      log.error("[ERROR] - Emitter failed for game {}: {}", gameId, e.getMessage());
      cleanup.run();
    });

    return emitter;
  }

  private void broadcast(int gameId, String data) {
    List<SseEmitter> emitters = this.gameEmitters.get(gameId);
    if (emitters == null) {
      return;
    }

    SseEmitter.SseEventBuilder event = SseEmitter.event()
        .name("message")
        .data(data);

    for (SseEmitter emitter : emitters) {
      try {
        emitter.send(event);
      } catch (IOException e) {
        emitter.completeWithError(e);
      }
    }
  }

  private CompletableFuture<WebSocket> connectToGame(int gameId, Consumer<String> onMessage) {
    return this.httpClient
        .newWebSocketBuilder()
        .buildAsync(getWebSocketUriForGame(gameId),
            new CallbackWebSocketListener(onMessage, gameId));
  }

  private URI getWebSocketUriForGame(int gameId) {
    return BASE_WEBSOCKET_URI.resolve("/api/v1/game/push/subscribe/gameday/%s".formatted(gameId));
  }

  @RequiredArgsConstructor
  private class CallbackWebSocketListener implements WebSocket.Listener {

    private final Consumer<String> callback;
    private final int gameId;

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
      callback.accept(data.toString());
      return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    @Override
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
      log.info("WebSocket for game {} closed: {} ({})", gameId, reason, statusCode);
      List<SseEmitter> emitters = gameEmitters.get(gameId);
      if (emitters != null) {
        SseEmitter.SseEventBuilder event = SseEmitter.event()
            .name("game_over")
            .data(reason);

        for (SseEmitter emitter : emitters) {
          try {
            emitter.send(event);
            emitter.complete();
          } catch (IOException e) {
            emitter.completeWithError(e);
          }
        }
      }

      gameEmitters.remove(gameId);
      return CompletableFuture.completedFuture(null);
    }

    @Override
    public void onError(WebSocket webSocket, Throwable error) {
      log.error("[ERROR] - WebSocket error for game {}: {}", gameId, error.getMessage());
      WebSocket.Listener.super.onError(webSocket, error);
    }

  }

}
