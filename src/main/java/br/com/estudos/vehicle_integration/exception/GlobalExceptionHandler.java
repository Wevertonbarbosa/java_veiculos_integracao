package br.com.estudos.vehicle_integration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.client.ResourceAccessException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Centraliza o tratamento das falhas de integração externa.
  @ExceptionHandler(ExternalApiException.class)
  public ResponseEntity<Map<String, Object>> handleExternalApiException(
      ExternalApiException ex) {

    Map<String, Object> response = Map.of(
        "status", HttpStatus.BAD_GATEWAY.value(),
        "error", "Bad Gateway",
        "message", ex.getMessage());

    // Informa que nossa aplicação recebeu uma falha da API externa.
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response);
  }

  // Centraliza o tratamento de falhas de comunicação, incluindo timeout.
  // Trata falhas de comunicação e timeout.
  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<Map<String, Object>> handleTimeout(
      ResourceAccessException ex) {

    Map<String, Object> response = Map.of(
        "status", HttpStatus.GATEWAY_TIMEOUT.value(),
        "error", "Gateway Timeout",
        "message", "A API externa demorou para responder.");

    return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(response);
  }


  // Centraliza o tratamento de falhas do Circuit Breaker.
  // Falha ao tentar executar uma chamada quando o Circuit Breaker está aberto/OPEN.
  @ExceptionHandler(CallNotPermittedException.class)
  public ResponseEntity<Map<String, Object>> handleCircuitBreakerOpen(
      CallNotPermittedException ex) {

    // Informa que a integração está temporariamente bloqueada.
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(Map.of(
            "error", "Service Unavailable",
            "message", "A integração está temporariamente indisponível.",
            "status", 503));
  }

}