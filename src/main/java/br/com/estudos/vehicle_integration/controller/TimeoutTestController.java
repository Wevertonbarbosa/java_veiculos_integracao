package br.com.estudos.vehicle_integration.controller;

import br.com.estudos.vehicle_integration.service.TimeoutTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeoutTestController {

  private final TimeoutTestService timeoutTestService;

  public TimeoutTestController(TimeoutTestService timeoutTestService) {
    this.timeoutTestService = timeoutTestService;
  }

  @GetMapping("/timeout-test")
  public String testTimeout() {

    // Inicia a chamada à API lenta.
    return timeoutTestService.callSlowApi();
  }
}