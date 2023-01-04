package com.explosion204.aperture.sandbox3.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/multipart")
@Slf4j
public class MultipartRestController {
  @PostMapping("/upload")
  public ResponseEntity<Void> uploadFile(@RequestPart("filename") String filename,
                                         @RequestPart("file") MultipartFile file) {

    log.info("File name: {}", filename);
    log.info("File size: {}", file.getSize());
    log.info("File content type: {}", file.getContentType());

    return ResponseEntity.ok().build();
  }
}
