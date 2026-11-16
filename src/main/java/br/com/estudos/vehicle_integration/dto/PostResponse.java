package br.com.estudos.vehicle_integration.dto;

public record PostResponse(
    Long userId,
    Long id,
    String title,
    String body) {
}