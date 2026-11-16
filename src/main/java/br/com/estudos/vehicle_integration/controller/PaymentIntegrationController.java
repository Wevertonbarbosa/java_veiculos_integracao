
package br.com.estudos.vehicle_integration.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import br.com.estudos.vehicle_integration.service.PaymentIntegrationService;

@RestController
public class PaymentIntegrationController {

  private final PaymentIntegrationService paymentIntegrationService;

  public PaymentIntegrationController(
      PaymentIntegrationService paymentIntegrationService) {
    this.paymentIntegrationService = paymentIntegrationService;
  }

  @PostMapping("/payments")
  public String processPayment(
      @RequestHeader("Idempotency-Key") String idempotencyKey,
      @RequestBody Map<String, Object> request) {

    return paymentIntegrationService.processPayment(
        request,
        idempotencyKey);
  }
}
