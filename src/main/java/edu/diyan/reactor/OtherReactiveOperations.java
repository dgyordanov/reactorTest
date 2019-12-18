package edu.diyan.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class OtherReactiveOperations {

    public static void main(String[] args) {
        // get the flux which will emit first an element
        Flux.first(
                FluxGenerator.u1u2Flux(),
                FluxGenerator.fooBarFluxWithDelay(1)
        ).subscribe(System.out::println);

        monoOfNullableElement(null)
                .subscribe(System.out::println);
    }

    static Mono<User> monoOfNullableElement(User nullableUser) {
        return Mono.justOrEmpty(nullableUser);
    }

}
