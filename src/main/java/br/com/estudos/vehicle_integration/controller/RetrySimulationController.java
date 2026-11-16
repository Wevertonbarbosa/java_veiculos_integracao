package br.com.estudos.vehicle_integration.controller;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetrySimulationController {

  private final AtomicInteger attempts = new AtomicInteger(0);

  // FALHA SEMPRE
  @GetMapping("/simulation/retry")
  public Map<String, String> simulateTemporaryFailure() {

    // Conta quantas vezes o endpoint foi chamado.
    int currentAttempt = attempts.incrementAndGet();

    System.out.println("API simulada recebeu a chamada " + currentAttempt);

    // Simula uma API que permanece indisponível.
    throw new IllegalStateException("API externa indisponível.");
  }

  // FALHA TEMPORÁRIA 2X
  // @GetMapping("/simulation/retry")
  // public Map<String, String> simulateTemporaryFailure() {

  // int currentAttempt = attempts.incrementAndGet();

  // System.out.println("API simulada recebeu a chamada " + currentAttempt);

  // if (currentAttempt <= 2) {
  // throw new IllegalStateException("API externa indisponível.");
  // }

  // return Map.of("message", "API respondeu com sucesso!");
  // }
}