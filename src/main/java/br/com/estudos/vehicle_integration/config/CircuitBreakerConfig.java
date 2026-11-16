
package br.com.estudos.vehicle_integration.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;

@Configuration
public class CircuitBreakerConfig {

  @Bean
  public CircuitBreaker vehicleCircuitBreaker() {

    // Define as regras de funcionamento do Circuit Breaker.
    io.github.resilience4j.circuitbreaker.CircuitBreakerConfig config = io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
        .custom()

        // Analisa as últimas quatro chamadas.
        .slidingWindowSize(4)

        // Exige pelo menos duas chamadas para avaliar as falhas.
        .minimumNumberOfCalls(2)

        // Abre o circuito quando 50% das chamadas falham.
        .failureRateThreshold(50)

        // Mantém o circuito aberto durante dez segundos.
        .waitDurationInOpenState(Duration.ofSeconds(10))

        // Permite duas chamadas de teste no estado HALF-OPEN.
        .permittedNumberOfCallsInHalfOpenState(2)

        // Utiliza uma janela baseada na quantidade de chamadas.
        .slidingWindowType(SlidingWindowType.COUNT_BASED)

        .build();

    // Cria e disponibiliza o Circuit Breaker para a aplicação.
    return CircuitBreaker.of("vehicleService", config);
  }
}
