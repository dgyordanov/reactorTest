package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

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
                .onErrorResume(e -> Flux.just("E", "F"))
                // just to see what is being emitted
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                // expect the fallback value
                .expectNext("A", "E", "F")
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
                .repeat(2)
                // just to see what is being emitted
                .doOnNext(System.out::println);

        StepVerifier.create(flux)
                .expectSubscription()
                // expect the fallback value
                .expectNext("A", "E", "A", "E", "A", "E")
                .verifyComplete();
    }

    private Mono<String> customMap(String a) {
        if (a.equals("B")) {
            throw new RuntimeException("ERROR");
        }
        return Mono.just(a);
    }

    @Test
    public void testRetry() {
        AtomicBoolean errored = new AtomicBoolean(false);

        var erroredFlux = Flux.create(sink -> {
            if (!errored.get()) {
                errored.set(true);
                sink.error(new RuntimeException("test retry"));
                System.out.println("Error....");
            } else {
                System.out.println("Emit element");
                sink.next(1);
            }
            sink.complete();
        });

        // retry() - resubscribe only on error
        // repeat() - resubscribe on any completion
        // retryBackoff() - retry after duration
        var erroredRetryFlux = erroredFlux
                .retry();
                //.retryBackoff(3, Duration.ofSeconds(3));

        StepVerifier.create(erroredRetryFlux)
                .expectNext(1)
                .expectComplete();
    }

}
