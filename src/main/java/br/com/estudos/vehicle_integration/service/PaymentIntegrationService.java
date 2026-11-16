
package br.com.estudos.vehicle_integration.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
public class PaymentIntegrationService {

  private final RestClient restClient;

  public PaymentIntegrationService(RestClient restClient) {
    this.restClient = restClient;
  }

  public String processPayment(
      Map<String, Object> request,
      String idempotencyKey) {

    int maxAttempts = 3;
    long waitTime = 1000;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {

      try {
        System.out.println("Tentativa de pagamento: " + attempt);

        return restClient.post()
            .uri("http://localhost:8080/simulation/payment")
            .header("Idempotency-Key", idempotencyKey)
            .body(request)
            .retrieve()
            .body(String.class);

      } catch (ResourceAccessException ex) {

        if (attempt == maxAttempts) {
          throw ex;
        }

        try {
          System.out.println(
              "Timeout na tentativa " + attempt
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

    throw new IllegalStateException("Não foi possível processar o pagamento.");
  }

}
