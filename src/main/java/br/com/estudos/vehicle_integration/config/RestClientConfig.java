package br.com.estudos.vehicle_integration.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  public RestClient restClient() {

    // Define o tempo máximo para estabelecer a conexão.
    //timeout de conexão
    HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3)) //connectTimeout	Tempo máximo para estabelecer a conexão com o servidor
        .build();



    // Configura o tempo máximo de espera pela resposta.
    JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
    // Tempo máximo de espera pelos dados da resposta
    // timeout de leitura
    requestFactory.setReadTimeout(Duration.ofSeconds(3));

    // Disponibiliza o cliente HTTP para a aplicação.
    return RestClient.builder()
        .baseUrl("https://jsonplaceholder.typicode.com")
        .requestFactory(requestFactory)
        .build();
  }
}