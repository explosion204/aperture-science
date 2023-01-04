package com.explosion204.aperture.sandbox3.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsRestController {
  private final AsyncTaskExecutor executor;

  @GetMapping
  public ResponseEntity<ResponseBodyEmitter> getEvents() {
    final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
    executor.submit(() -> generateEvents(emitter));
    return ResponseEntity.ok(emitter);
  }

  @GetMapping("/sse")
  public SseEmitter getSseEvents() {
    final SseEmitter emitter = new SseEmitter();
    executor.submit(() -> generateEvents(emitter));
    return emitter;
  }

  @GetMapping("/file")
  public StreamingResponseBody getFile() {
    return outputStream -> {
      final Resource resource = new ClassPathResource("files/sample.mp3");
      resource.getInputStream().transferTo(outputStream);
    };
  }

  @SneakyThrows
  private void generateEvents(ResponseBodyEmitter emitter) {
    int counter = 0;

    while (counter < 10) {
      emitter.send(counter++);
      Thread.sleep(1000);
    }

    emitter.complete();
  }
}
