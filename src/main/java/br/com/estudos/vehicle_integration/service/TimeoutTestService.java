package br.com.estudos.vehicle_integration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TimeoutTestService {

  private final RestClient restClient;

  public TimeoutTestService(RestClient restClient) {
    this.restClient = restClient;
  }

  public String callSlowApi() {

    // Chama o endpoint local usando o timeout configurado.
    return restClient.get()
        .uri("http://localhost:8080/simulation/slow")
        .retrieve()
        .body(String.class);
  }
}