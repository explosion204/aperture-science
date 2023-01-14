package com.explosion204.reactor;

import lombok.extern.slf4j.Slf4j;
import org.ietf.jgss.GSSException;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.stream.Stream;

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
  void testSubscriberOfNumbersRange() {
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

  @Test
  void testSubscriberOfInterval() {
    StepVerifier.withVirtualTime(() -> Flux.interval(Duration.ofHours(1)))
            .expectSubscription()
            .expectNoEvent(Duration.ofMinutes(30))
            .thenAwait(Duration.ofHours(1))
            .expectNext(0L)
            .thenAwait(Duration.ofHours(1))
            .expectNext(1L)
            .thenCancel()
            .verify();
  }

  @Test
  void testSubscriberWithRateLimit() {
    final List<Integer> data = List.of(1, 2, 3, 4, 5);
    final Flux<Integer> flux = Flux.fromIterable(data)
            .log()
            .limitRate(2);

    flux.subscribe(num -> log.info("Value: {}", num));

    StepVerifier.create(flux)
            .expectNextSequence(data)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithConnectableFlux() {
    final ConnectableFlux<Integer> flux = Flux.range(1, 10)
            .log()
            .delayElements(Duration.ofMillis(100))
            .publish();

    StepVerifier.create(flux)
            .then(flux::connect)
            .thenConsumeWhile(num -> num < 7)
            .expectNext(7, 8, 9, 10)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithConnectableFluxAndAutoConnect() {
    final Flux<Integer> flux = Flux.range(1, 10)
            .log()
            .delayElements(Duration.ofMillis(100))
            .publish()
            .autoConnect(2);

    StepVerifier.create(flux)
            .then(flux::subscribe)
            .thenConsumeWhile(num -> num < 7)
            .expectNext(7, 8, 9, 10)
            .verifyComplete();
  }

  @Test
  void testSubscribeOn() {
    final Flux<Integer> flux = Flux.range(1, 4)
            .subscribeOn(Schedulers.boundedElastic())
            .subscribeOn(Schedulers.single())
            .map(num -> {
              log.info("[1] Num - {}, thread - {}", num, Thread.currentThread().getName());
              return num;
            })
            .map(num -> {
              log.info("[2] Num - {}, thread - {}", num, Thread.currentThread().getName());
              return num;
            });

    StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1, 2, 3, 4)
            .verifyComplete();
  }

  @Test
  void testPublishOn() {
    final Flux<Integer> flux = Flux.range(1, 4)
            .doOnSubscribe(sub -> log.info("Subscribed! Thread - {}", Thread.currentThread().getName()))
            .publishOn(Schedulers.boundedElastic())
            .map(num -> {
              log.info("[1] Num - {}, thread - {}", num, Thread.currentThread().getName());
              return num;
            })
            .publishOn(Schedulers.single())
            .map(num -> {
              log.info("[2] Num - {}, thread - {}", num, Thread.currentThread().getName());
              return num;
            });

    StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1, 2, 3, 4)
            .verifyComplete();
  }

  @Test
  void testPublishOnAndSubscribeOn() {
    final Flux<Integer> flux = Flux.range(1, 4)
            .doOnSubscribe(sub -> log.info("Subscribed! Thread - {}", Thread.currentThread().getName()))
            .map(num -> {
              log.info("[1] Num - {}, thread - {}", num, Thread.currentThread().getName());
              return num;
            })
            .publishOn(Schedulers.single())
            .map(num -> {
              log.info("[2] Num - {}, thread - {}", num, Thread.currentThread().getName());
              return num;
            }).subscribeOn(Schedulers.parallel());

    StepVerifier.create(flux)
            .expectSubscription()
            .expectNext(1, 2, 3, 4)
            .verifyComplete();
  }

  @Test
  void testSubscriberOfCallable() {
    final Callable<Stream<String>> fileCallable = () -> {
      log.info("Reading file from thread {}", Thread.currentThread().getName());
      final URL fileUrl = FluxTest.class.getResource("/test.txt");
      return Files.lines(Paths.get(fileUrl.toURI()));
    };

    final Flux<String> flux = Mono.fromCallable(fileCallable)
            .publishOn(Schedulers.parallel())
            .flatMapMany(Flux::fromStream);

    StepVerifier.create(flux)
            .expectSubscription()
            .expectNext("hello")
            .expectNext("there")
            .expectNext("general")
            .expectNext("kenobi")
            .expectComplete()
            .verify();
  }

  @Test
  void testSubscriberOfConcattedFlux() {
    final Flux<Integer> firstFlux = Flux.just(1, 2, 3);
    final Flux<Integer> secondFlux = Flux.just(4, 5, 6);
    final Flux<Integer> concattedFlux = Flux.concat(firstFlux, secondFlux);

    StepVerifier.create(concattedFlux)
            .expectNext(1, 2, 3, 4, 5, 6)
            .verifyComplete();
  }

  @Test
  void testSubscriberOfCombinedLastFlux() {
    final Flux<Integer> firstFlux = Flux.just(1, 2, 3);
    final Flux<Integer> secondFlux = Flux.just(4, 5, 6);
    final Flux<Integer> combinedFlux = Flux.combineLatest(firstFlux, secondFlux, Integer::sum);

    combinedFlux.subscribe(num -> log.info("Value: {}", num));
  }

  @Test
  void testSubscriberOfMergedFlux() {
    final Flux<Integer> firstFlux = Flux.just(1, 2, 3);
    final Flux<Integer> secondFlux = Flux.just(4, 5, 6);
    final Flux<Integer> mergedFlux = Flux.merge(firstFlux, secondFlux);

    mergedFlux.subscribe(num -> log.info("Value: {}", num));
  }

  @Test
  void testSubscriberOfMergedSequentialFlux() {
    final Flux<Integer> firstFlux = Flux.just(1, 2, 3);
    final Flux<Integer> secondFlux = Flux.just(4, 5, 6);
    final Flux<Integer> mergedFlux = Flux.mergeSequential(firstFlux, secondFlux, firstFlux);

    StepVerifier.create(mergedFlux)
            .expectSubscription()
            .expectNext(1, 2, 3, 4, 5, 6, 1, 2, 3)
            .verifyComplete();
  }

  @Test
  void testSubscriberWithConcatDelayError() {
    final Flux<Integer> firstFlux = Flux.just(1, 2, 3)
            .map(num -> {
              if (num == 3) {
                throw new IllegalArgumentException();
              }

              return num;
            });
    final Flux<Integer> secondFlux = Flux.just(4, 5, 6);
    final Flux<Integer> mergedFlux = Flux.concatDelayError(firstFlux, secondFlux);

    StepVerifier.create(mergedFlux)
            .expectSubscription()
            .expectNext(1, 2, 4, 5, 6)
            .expectError(IllegalArgumentException.class)
            .verify();
  }

  @Test
  void testSubscriberOfFlatMappedFlux() {
    final Flux<Integer> flux = Flux.just(1, 2, 3);
    final Mono<Integer> mono = Mono.just(4);
    final Flux<Integer> flatMappedFlux = flux.flatMap(num -> mono);

    StepVerifier.create(flatMappedFlux)
            .expectSubscription()
            .expectNext(4, 4, 4)
            .verifyComplete();
  }

  @Test
  void testSubscriberOfZippedFlux() {
    final Flux<Integer> firstFlux = Flux.just(1, 2, 3, 10);
    final Flux<Integer> secondFlux = Flux.just(4, 5, 6);
    final Flux<Integer> zippedFlux = Flux.zip(firstFlux, secondFlux, Integer::sum);

    StepVerifier.create(zippedFlux)
            .expectSubscription()
            .expectNext(5, 7, 9)
            .verifyComplete();
  }
}
