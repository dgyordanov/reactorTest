package edu.diyan.reactor;

import org.junit.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxTest1Test {

    @Test
    public void expectFooBarComplete() {
        StepVerifier.create(FluxTest1.fooBarFluxFromValues())
                .expectNext("foo", "bar")
                .verifyComplete();
    }

    @Test
    public void expectError() {
        StepVerifier.create(FluxTest1.errorFlux())
                .verifyError(IllegalStateException.class);
    }

    @Test
    public void expectUsernames() {
        StepVerifier.create(FluxTest1.u1u2Flux())
                .assertNext(u -> u.getUsername().equals("u1"))
                .assertNext(u -> u.getUsername().equals("u2"))
                .verifyComplete();
    }

    @Test
    public void expect10ElementsSlow() {
        StepVerifier.create(FluxTest1.slowFlux(10, 1))
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void expect3600ElementsSlow() {
        StepVerifier.withVirtualTime(
                () -> FluxTest1.slowFlux(3600, 1))
                .expectSubscription()
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(3600)
                .verifyComplete();
    }


}
