package br.com.estudos.vehicle_integration.exception;

public class ExternalApiException extends RuntimeException {

  // Permite preservar a causa original do erro.
  public ExternalApiException(String message, Throwable cause) {
    super(message, cause);
  }
}