package edu.diyan.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.Random;

class FluxGenerator {

    static Flux<String> emptyFlux() {
        return Flux.empty();
    }

    static Flux<String> fooBarFluxFromValues() {
        return Flux.just("foo", "bar");
    }

    static Flux<String> fooBarFluxFromList() {
        return Flux.fromIterable(Arrays.asList("foo", "bar"));
    }

    static Flux<String> fooBarFluxWithDelay(int seconds) {
        return Flux.just("foo", "bar")
                .delayElements(Duration.ofSeconds(seconds));
    }

    static Flux<User> u1u2Flux() {
        return Flux.just(new User("u1"), new User("u2"));
    }

    static Flux<Integer> slowFlux(int numberOfElements, int durationInSec) {
        return Flux.range(1, numberOfElements)
                .delayElements(Duration.ofSeconds(durationInSec));
    }

    static Flux<Integer> interval10RandomInt(Duration interval) {
        return Flux.interval(interval)
                .map(val -> new Random().nextInt(100))
                .take(10);
    }

    static Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

    static Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100))
                .take(10);
    }

}


