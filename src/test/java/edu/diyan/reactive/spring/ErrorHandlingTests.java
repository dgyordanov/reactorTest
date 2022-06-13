package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ErrorHandlingTests {

    @Test
    public void testCatchAndReturnStaticValue() {
        var flux = Flux.just("A", "B", "C", "D")
                .map(a -> {
                    if (a.equals("B")) {
                        throw new RuntimeException("ERROR");
                    }
                    return a;
                })
                .onErrorResume(e -> Mono.just("E"))
                // just to see what is being emitted
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                // expect the fallback value
                .expectNext("A", "E")
                .verifyComplete();
    }

    @Test
    public void testPropagateError() {
        var flux = Flux.just("A", "B", "C", "D")
                .concatMap(e -> {
//                    try {
                        return this.customMap(e)
                                .onErrorResume(error -> Mono.just("F"));
//                    } catch (Exception ex) {
//                        return Mono.just("G");
//                    }
                })
                .onErrorResume(e -> Mono.just("E"))
                // To resubscribe on completion
                //.repeat()
                // just to see what is being emitted
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectSubscription()
                // expect the fallback value
                .expectNext("A", "G", "C", "D")
                .verifyComplete();
    }

    private Mono<String> customMap(String a) {
        if (a.equals("B")) {
            throw new RuntimeException("ERROR");
        }
        return Mono.just(a);
    }
}
