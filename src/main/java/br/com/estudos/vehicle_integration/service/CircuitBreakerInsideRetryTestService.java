
package br.com.estudos.vehicle_integration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

@Service
public class CircuitBreakerInsideRetryTestService {

  private final RestClient restClient;
  private final CircuitBreaker circuitBreaker;

  public CircuitBreakerInsideRetryTestService(
      RestClient restClient,
      CircuitBreaker circuitBreaker) {
    this.restClient = restClient;
    this.circuitBreaker = circuitBreaker;
  }

  public String callWithRetry() {

    int maxAttempts = 3;
    long waitTime = 1000;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {

      try {
        System.out.println("Tentativa " + attempt);

        // Cada tentativa passa individualmente pelo Circuit Breaker.
        return circuitBreaker.executeSupplier(() -> restClient.get()
            .uri("http://localhost:8080/simulation/retry")
            .retrieve()
            .body(String.class));

      } catch (HttpServerErrorException | ResourceAccessException ex) {

        if (attempt == maxAttempts) {
          throw ex;
        }

        try {
          System.out.println(
              "Falha na tentativa " + attempt
                  + ". Aguardando " + waitTime + " ms...");

          Thread.sleep(waitTime);
          waitTime = Math.min(waitTime * 2, 2000);

        } catch (InterruptedException interruptedEx) {
          Thread.currentThread().interrupt();

          throw new IllegalStateException(
              "Operação interrompida durante o Retry.",
              interruptedEx);
        }
      }
    }

    throw new IllegalStateException(
        "Não foi possível concluir a operação.");
  }
}
