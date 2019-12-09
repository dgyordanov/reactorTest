package edu.diyan.reactor;

import reactor.core.publisher.Mono;

import java.time.Duration;

public class MonoTest1 {

    public static void main(String[] args) {
        Mono.just(1)
                .map(integer -> "foo" + integer)
                .or(Mono
                        .delay(Duration.ofMillis(100))
                        .map(String::valueOf)
                )
                .subscribe(System.out::println);
    }

    Mono<String> emptyMono() {
        return Mono.empty();
    }

    //  Return a Mono that never emits any signal
    Mono<String> monoWithNoSignal() {
        return Mono.never();
    }

    // Return a Mono that contains a "foo" value
    Mono<String> fooMono() {
        return Mono.just("foo");
    }

    // Create a Mono that emits an IllegalStateException
    Mono<String> errorMono() {
        return Mono.error(new IllegalStateException());
    }

}
