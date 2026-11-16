package br.com.estudos.vehicle_integration.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SlowApiController {

  @GetMapping("/simulation/slow")
  public Map<String, String> simulateSlowResponse() throws InterruptedException {

    // Simula uma API que demora 5 segundos para responder.
    Thread.sleep(5000);

    return Map.of("message", "Resposta da API lenta.");
  }
}