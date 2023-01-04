package com.explosion204.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
class MonoTest {

  @Test
  void testEmptySubscriber() {
    final String data = "hello there!";
    final Mono<String> mono = Mono.just(data).log();

    mono.subscribe();

    StepVerifier.create(mono)
            .expectNext(data)
            .verifyComplete();
  }

  @Test
  void testRegularSubscriber() {
    final String data = "hello there!";
    final Mono<String> mono = Mono.just(data).log();

    mono.subscribe(s -> log.info("Received value: {}", s));

    StepVerifier.create(mono)
            .expectNext(data)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithError() {
    final Mono<String> mono = Mono.error(IllegalArgumentException::new);

    mono.subscribe(
            s -> log.info("Received value: {}", s),
            e -> log.error("Caught an exception: ", e)
    );

    StepVerifier.create(mono)
            .expectError(IllegalArgumentException.class)
            .verify();
  }

  @Test
  void testSubscriberWithCompletion() {
    final String data = "hello there!";
    final Mono<String> mono = Mono.just(data).log();

    mono.subscribe(
            s -> log.info("Received value: {}", s),
            Throwable::printStackTrace,
            () -> log.info("Mono is completed")
    );

    StepVerifier.create(mono)
            .expectNext(data)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithDoOnSubscribeNextSuccess() {
    final String data = "hello there!";
    final Mono<String> mono = Mono.just(data)
            .doOnSubscribe(s -> log.info("Subscribed"))
            .doOnNext(s -> log.info("Value: {}", s))
            .doOnSuccess(s -> log.info("Success"));

    mono.subscribe(s -> log.info("Received value: {}", s));

    StepVerifier.create(mono)
            .expectNext(data)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithDoOnError() {
    final Mono<Object> mono = Mono.error(IllegalArgumentException::new)
            .doOnError(e -> log.error("Caught an exception:", e))
            .doOnNext(s -> log.info("Value: {}", s));

    mono.subscribe(s -> log.info("Received value: {}", s));

    StepVerifier.create(mono)
            .expectError(IllegalArgumentException.class)
            .verify();
  }

  @Test
  void testSubscriberWithDoOnErrorResume() {
    final String fallbackValue = "fallback";
    final Mono<Object> mono = Mono.error(IllegalArgumentException::new)
            .doOnError(e -> log.error("Caught an exception:", e))
            .onErrorResume(e -> Mono.just(fallbackValue))
            .doOnNext(s -> log.info("Value: {}", s));

    mono.subscribe(s -> log.info("Received value: {}", s));

    StepVerifier.create(mono)
            .expectNext(fallbackValue)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithDoOnErrorReturn() {
    final String fallbackValue = "fallback";
    final Mono<Object> mono = Mono.error(IllegalArgumentException::new)
            .doOnError(e -> log.error("Caught an exception:", e))
            .onErrorReturn(fallbackValue)
            .doOnNext(s -> log.info("Value: {}", s));

    mono.subscribe(s -> log.info("Received value: {}", s));

    StepVerifier.create(mono)
            .expectNext(fallbackValue)
            .verifyComplete();
  }
}
