package br.com.estudos.vehicle_integration.controller;

import br.com.estudos.vehicle_integration.dto.PostResponse;
import br.com.estudos.vehicle_integration.service.PostIntegrationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostIntegrationService postIntegrationService;

  public PostController(PostIntegrationService postIntegrationService) {
    this.postIntegrationService = postIntegrationService;
  }

  @GetMapping("/{id}")
  public PostResponse buscarPost(@PathVariable Long id) {
    return postIntegrationService.buscarPost(id);
  }
}