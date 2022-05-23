package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class TakeUntilTest {

    @Test
    public void take3ElementsTest() {
        var nums = Flux.just(1, 2, 3, 4, 5).take(3);
        StepVerifier.create(nums)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

    @Test
    public void takeUntilTest() {
        var nums = Flux.just(1, 2, 3, 4, 5)
                .takeUntil(e -> e >= 2);

        StepVerifier.create(nums)
                .expectNext(1, 2)
                .verifyComplete();
    }

    @Test
    public void takeUntilDurationTest() {
        var nums = Flux.just(1, 2, 3, 4, 5, 6)
                .delayElements(Duration.ofMillis(200))
                .take(Duration.ofMillis(500));

        StepVerifier.create(nums)
                .expectNext(1, 2)
                .verifyComplete();
    }

}
