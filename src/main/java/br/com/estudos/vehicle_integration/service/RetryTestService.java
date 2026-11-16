
package br.com.estudos.vehicle_integration.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Service
public class RetryTestService {

  private final RestClient restClient;

  public RetryTestService(RestClient restClient) {
    this.restClient = restClient;
  }

  public String callWithRetry() {

    int maxAttempts = 3;
    long waitTime = 1000;
    long maxWaitTime = 2000;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        System.out.println("Tentativa " + attempt);

        // Executa a chamada à API simulada.
        return restClient.get()
            .uri("http://localhost:8080/simulation/retry")
            .retrieve()
            .body(String.class);

      } catch (HttpServerErrorException | ResourceAccessException ex) {

        // Não realiza novas tentativas após atingir o limite.
        if (attempt == maxAttempts) {
          throw ex;
        }

        try {
          System.out.println(
              "Falha temporária na tentativa " + attempt
                  + ". Aguardando " + waitTime + " ms...");

          // Aguarda antes de realizar uma nova tentativa.
          Thread.sleep(waitTime);

          // Dobra o intervalo, respeitando o limite máximo.
          waitTime = Math.min(waitTime * 2, maxWaitTime);

        } catch (InterruptedException interruptedEx) {

          // Preserva o sinal de interrupção da thread.
          Thread.currentThread().interrupt();

          throw new IllegalStateException(
              "A operação foi interrompida durante o Retry.",
              interruptedEx);
        }
      }
    }

    throw new IllegalStateException("Não foi possível concluir a operação.");
  }
}
