package edu.diyan.reactor;

import lombok.Value;
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

    static Flux<User> u1u2Flux() {
        return Flux.just(new User("u1"), new User("u2"));
    }

    static Flux<Integer> slowFlux(int numberOfElements, int durationInSec) {
        return Flux.range(1, numberOfElements)
                .delayElements(Duration.ofSeconds(durationInSec));
    }

    static Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

    static Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100))
                .take(10);
    }

}

@Value
class User {
    String username;
}
