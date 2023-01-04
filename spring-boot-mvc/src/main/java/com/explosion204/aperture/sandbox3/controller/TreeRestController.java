package com.explosion204.aperture.sandbox3.controller;

import com.explosion204.aperture.sandbox3.controller.model.Tree;
import com.explosion204.aperture.sandbox3.service.TreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tree")
@RequiredArgsConstructor
public class TreeRestController {
  private final TreeService treeService;

  @PostMapping(value = "/describe", consumes = "text/plain")
  public ResponseEntity<String> describeTree(@RequestBody Tree tree) {
    final String description = treeService.generateDescription(tree);
    return ResponseEntity.ok(description);
  }
}
