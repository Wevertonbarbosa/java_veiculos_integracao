
package br.com.estudos.vehicle_integration.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.estudos.vehicle_integration.service.CircuitBreakerInsideRetryTestService;
import br.com.estudos.vehicle_integration.service.CircuitBreakerTestService;

@RestController
public class CircuitBreakerTestController {

  private final CircuitBreakerTestService circuitBreakerTestService;
  private final CircuitBreakerInsideRetryTestService retryWithCircuitBreakerTestService;

  public CircuitBreakerTestController(
      CircuitBreakerTestService circuitBreakerTestService,
      CircuitBreakerInsideRetryTestService retryWithCircuitBreakerTestService) {
    this.circuitBreakerTestService = circuitBreakerTestService;
    this.retryWithCircuitBreakerTestService = retryWithCircuitBreakerTestService;
  }

  @GetMapping("/circuit-breaker-test")
  public String testCircuitBreaker() {

    // Executa a chamada protegida pelo Circuit Breaker.
    return circuitBreakerTestService.callWithCircuitBreaker();
  }

  @GetMapping("/circuit-breaker-state")
  public Map<String, String> getCircuitState() {

    // Retorna o estado atual do circuito.
    return Map.of(
        "state", circuitBreakerTestService.getCircuitState());
  }

  @GetMapping("/retry-with-circuit-breaker-test")
  public String testRetryWithCircuitBreaker() {
    return retryWithCircuitBreakerTestService.callWithRetry();
  }
}
