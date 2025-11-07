package com.spasbo.mlb.configuration;

import java.net.http.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configures various HTTP client objects.
 */
@Configuration
public class HttpClientConfiguration {

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public HttpClient httpClient() {
    return HttpClient.newHttpClient();
  }
}
