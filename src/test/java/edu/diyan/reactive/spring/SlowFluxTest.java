package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class SlowFluxTest {

    @Test
    public void timeoutTest() {
        var slow = Flux.just(1, 2, 3)
                .delayElements(Duration.ofSeconds(1))
                .timeout(Duration.ofMillis(500))
                .onErrorReturn(4);

        StepVerifier.create(slow)
                .expectNext(4)
                .verifyComplete();
    }

    @Test
    public void firstFluxTest() {
        var faster = Flux.just(1, 2, 3)
                .delayElements(Duration.ofMillis(50));
        var slow = Flux.just(4, 5, 6)
                .delayElements(Duration.ofMillis(110));

        // first() picks the flux the emits a signal first and cancels the others
        var fistFlux = Flux.first(faster, slow);

        StepVerifier.create(fistFlux)
                .expectNext(1, 2, 3)
                .verifyComplete();
    }

}
