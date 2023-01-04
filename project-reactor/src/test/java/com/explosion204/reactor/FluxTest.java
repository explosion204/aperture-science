package com.explosion204.reactor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@Slf4j
class FluxTest {
  @Test
  void testEmptySubscriber() {
    final String[] data = { "hello", "there", "!" };
    final Flux<String> flux = Flux.just(data);

    flux.subscribe();

    StepVerifier.create(flux)
            .expectNext(data)
            .verifyComplete();
  }

  @Test
  void testSubscriberOfNumbers() {
    final Flux<Integer> flux = Flux.range(1, 5);

    flux.subscribe(num -> log.info("Value: {}", num));

    StepVerifier.create(flux)
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete();
  }

  @Test
  void testSubscriberFromList() {
    final List<Integer> data = List.of(1, 2, 3, 4, 5);
    final Flux<Integer> flux = Flux.fromIterable(data);

    flux.subscribe(num -> log.info("Value: {}", num));

    StepVerifier.create(flux)
            .expectNextSequence(data)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithError() {
    final List<Integer> data = List.of(1, 2, 3, 4, 5);
    final Flux<Integer> flux = Flux.fromIterable(data)
            .map(num -> {
              if (num == 4) {
                throw new IllegalArgumentException();
              }

              return num;
            });

    flux.subscribe(
            num -> log.info("Value: {}", num),
            e -> log.error("Caught an exception:", e)
    );

    StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .expectError(IllegalArgumentException.class)
            .verify();
  }

  @Test
  void testSubscriberWithAvoidedErrorAndImplementedSubscriber() {
    final List<Integer> data = List.of(1, 2, 3, 4, 5);
    final Flux<Integer> flux = Flux.fromIterable(data)
            .map(num -> {
              if (num == 4) {
                throw new IllegalArgumentException();
              }

              return num;
            });

    flux.subscribeWith(new Subscriber<Integer>() {
      @Override
      public void onSubscribe(Subscription s) {
        s.request(2);
      }

      @Override
      public void onNext(Integer num) {
        log.info("Value: {}", num);
      }

      @Override
      public void onError(Throwable e) {
        log.error("Caught an exception:", e);
      }

      @Override
      public void onComplete() {
        log.info("Completed");
      }
    });
    
    StepVerifier.create(flux)
            .expectNext(1, 2, 3)
            .expectError(IllegalArgumentException.class)
            .verify();
  }
}
