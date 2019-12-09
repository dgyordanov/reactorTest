package edu.diyan.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;

public class FluxTest1 {

    static Flux<String> emptyFlux() {
        return Flux.empty();
    }

    static Flux<String> fooBarFluxFromValues() {
        return Flux.just("foo", "bar");
    }

    static Flux<String> fooBarFluxFromList() {
        return Flux.fromIterable(Arrays.asList("foo", "bar"));
    }

    static Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

    static Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100))
                .take(10);
    }

}
