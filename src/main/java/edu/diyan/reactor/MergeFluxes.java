package edu.diyan.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MergeFluxes {

    static <T> Flux<T> merge(Flux<T> f1, Flux<T> f2) {
        // Merge both fluxes and both produce at the same time
        return f1.mergeWith(f2);
    }

    static <T> Flux<T> concat(Flux<T> f1, Flux<T> f2) {
        return f1.concatWith(f2);
    }

    static <T> Flux<T> concat(Mono<T> m1, Mono<T> m2) {
        return m1.concatWith(m2);
    }

}
