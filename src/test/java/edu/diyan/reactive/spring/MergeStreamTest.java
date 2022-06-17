package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class MergeStreamTest {

    @Test
    public void mergeTest() {
        var fast = Flux.just(1, 2);
        var slow = Flux.just(3, 4)
                .delayElements(Duration.ofMillis(20));
        var average = Flux.just(5, 6)
                .delayElements(Duration.ofMillis(2));

        var merged = Flux.merge(fast, slow, average);

        StepVerifier.create(merged)
                .expectNext(1, 2, 5, 6, 3, 4)
                .expectComplete();
    }

    @Test
    public void mergeWithFlatMapTest() {
        var fast = Flux.just(1, 2);
        var slow = Flux.just(3, 4)
                .delayElements(Duration.ofMillis(20));
        var average = Flux.just(5, 6)
                .delayElements(Duration.ofMillis(2));

        var fluxes = Flux.just(fast, slow, average);

        var merged = fluxes.flatMap(e -> e);

        StepVerifier.create(merged)
                .expectNext(1, 2, 5, 6, 3, 4)
                .expectComplete();
    }


}
