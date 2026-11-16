
package br.com.estudos.vehicle_integration.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentSimulationController {

  private final AtomicInteger processedPayments = new AtomicInteger();

  // Armazena o resultado associado a cada chave de idempotência.
  private final Map<String, Map<String, Object>> paymentsByKey = new ConcurrentHashMap<>();

  @PostMapping("/simulation/payment")
  public Map<String, Object> simulatePayment(
      @RequestHeader("Idempotency-Key") String idempotencyKey,
      @RequestBody Map<String, Object> request) {

    AtomicBoolean isNewPayment = new AtomicBoolean(false);

    // Só cria um pagamento se a chave ainda não foi utilizada.
    Map<String, Object> payment = paymentsByKey.computeIfAbsent(
        idempotencyKey,
        key -> {
          isNewPayment.set(true);

          int paymentId = processedPayments.incrementAndGet();

          System.out.println("Pagamento processado: " + paymentId);

          return Map.of(
              "paymentId", paymentId,
              "amount", request.get("amount"),
              "status", "PROCESSED");
        });

    if (isNewPayment.get()) {
      try {
        // Simula a demora somente no primeiro processamento.
        Thread.sleep(5000);
      } catch (InterruptedException ex) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException(
            "Operação interrompida.", ex);
      }
    } else {
      System.out.println(
          "Chave já processada. Reutilizando resultado: "
              + idempotencyKey);
    }

    return payment;
  }

  @GetMapping("/simulation/payment/count")
  public Map<String, Integer> getPaymentCount() {
    return Map.of("totalProcessed", processedPayments.get());
  }
}
