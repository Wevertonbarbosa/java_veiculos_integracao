package br.com.estudos.vehicle_integration.service;

import br.com.estudos.vehicle_integration.dto.PostResponse;
import br.com.estudos.vehicle_integration.exception.ExternalApiException;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
public class PostIntegrationService {

  private final RestClient restClient;

  // injeção de dependência por construtor.
  public PostIntegrationService(RestClient restClient) {
    this.restClient = restClient;
  }

  public PostResponse buscarPost(Long id) {

    try {
      // Executa a consulta na API externa.
      return restClient.get()
          .uri("/posts/{id}", id)
          .retrieve()
          .body(PostResponse.class);

    } catch (HttpClientErrorException | HttpServerErrorException ex) {

      // Converte o erro do RestClient em uma exceção da aplicação.
      throw new ExternalApiException(
          "Falha ao consultar a API externa.",
          ex);
    } catch (ResourceAccessException ex) {

      // Trata falhas de comunicação, incluindo timeout.
      throw new ExternalApiException(
          "A API externa demorou para responder.",
          ex);


          
    }
  }
}