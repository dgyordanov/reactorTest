package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;

public class TransformTest {

    @Test
    public void name() {
        AtomicBoolean completed = new AtomicBoolean(false);
        Flux<Integer> flux = Flux.just(1, 2, 3)
                // Transform the flux as part of its assembly.
                // You can change scheduler etc.
                .transform(f -> f.doFinally(
                        signal -> completed.set(true)
                ));

        StepVerifier.create(flux).expectNextCount(3).verifyComplete();
        assertTrue(completed.get());
    }

}
