
package br.com.estudos.vehicle_integration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

@Service
public class CircuitBreakerTestService {

  private final RetryTestService retryTestService;
  private final CircuitBreaker circuitBreaker;

  public CircuitBreakerTestService(
      RetryTestService retryTestService,
      CircuitBreaker circuitBreaker) {

    this.retryTestService = retryTestService;
    this.circuitBreaker = circuitBreaker;
  }

  public String callWithCircuitBreaker() {

    try {

      // O Circuit Breaker envolve toda a operação de Retry.
      return circuitBreaker.executeSupplier(() -> {

        System.out.println("Executando operação protegida...");

        // O Retry controla as tentativas de comunicação.
        return retryTestService.callWithRetry();
      });

    } catch (CallNotPermittedException ex) {

      return fallback(ex);

    } catch (HttpServerErrorException | ResourceAccessException ex) {

      return fallback(ex);
    }
  }

  public String getCircuitState() {

    // Consulta o estado atual do Circuit Breaker.
    return circuitBreaker.getState().name();
  }

  private String fallback(Exception ex) {

    // Retorna uma resposta alternativa quando a integração falha.
    System.out.println("Executando Fallback: " + ex.getMessage());

    return """
        {
            "message": "Serviço temporariamente indisponível.",
            "fallback": true
        }
        """;
  }
}
