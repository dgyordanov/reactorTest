package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * thenMany() allow us to make sure elements from Flux A will be processed after elements from Flux B
 */
public class ThenManyTest {

    @Test
    public void testThenMany() {
        AtomicInteger lettersCounter = new AtomicInteger();
        AtomicInteger numsCounter = new AtomicInteger();

        Flux<String> lettersFlux = Flux.just("a", "b", "c")
                // doOnNext() just executes a behaviour and returns a Flux with same elements
                .doOnNext(e -> lettersCounter.incrementAndGet());
        Flux<String> numsFlux = Flux.just("1", "2", "3")
                .doOnNext(e -> numsCounter.incrementAndGet());
        var mergedFlux = lettersFlux.thenMany(numsFlux);

        StepVerifier.create(mergedFlux)
                // thenMany() ignores the elements from lettersFlux, but process elements from numsFlux only after lettersFlux completes
                .expectNext("1", "2", "3")
                .verifyComplete();

        // Make sure doOnNext() were executed from both streams
        assertEquals(3, lettersCounter.get());
        assertEquals(3, numsCounter.get());
    }

}
