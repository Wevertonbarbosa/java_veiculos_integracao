package br.com.estudos.vehicle_integration.controller;

import br.com.estudos.vehicle_integration.service.RetryTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryTestController {

  private final RetryTestService retryTestService;

  public RetryTestController(RetryTestService retryTestService) {
    this.retryTestService = retryTestService;
  }

  @GetMapping("/retry-test")
  public String testRetry() {

    // Executa a consulta com novas tentativas automáticas.
    return retryTestService.callWithRetry();
  }
}