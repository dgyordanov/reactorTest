package edu.diyan.reactor;

import reactor.core.publisher.Mono;

import java.time.Duration;

class MonoGenerator {

    public static void main(String[] args) {
        Mono.just(1)
                .map(integer -> "foo" + integer)
                .or(Mono
                        .delay(Duration.ofMillis(1000))
                        .map(String::valueOf)
                )
                .subscribe(System.out::println);
    }

    static Mono<String> emptyMono() {
        return Mono.empty();
    }

    //  Return a Mono that never emits any signal
    static Mono<String> monoWithNoSignal() {
        return Mono.never();
    }

    // Return a Mono that contains a "foo" value
    static Mono<String> fooMono() {
        return Mono.just("foo");
    }

    static Mono<User> userMono() {
        return Mono.just(new User("foo"));
    }

    // Create a Mono that emits an IllegalStateException
    static Mono<String> errorMono() {
        return Mono.error(new IllegalStateException());
    }

}
