package edu.diyan.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MergeFluxesTest {

    @Test
    public void testMerge() {
        Flux<String> merged = MergeFluxes.merge(
                FluxGenerator.fooBarFluxWithDelay(1),
                FluxGenerator.fooBarFluxWithDelay(1)
        );

        merged.subscribe(System.out::println);

        StepVerifier.create(merged)
                .expectNext("foo", "foo", "bar", "bar")
                .verifyComplete();
    }

    @Test
    public void testConcat() {
        Flux<String> concat = MergeFluxes.concat(
                FluxGenerator.fooBarFluxWithDelay(1),
                FluxGenerator.fooBarFluxWithDelay(1)
        );

        concat.subscribe(System.out::println);

        StepVerifier.create(concat)
                .expectNext("foo", "bar", "foo", "bar")
                .verifyComplete();
    }

    @Test
    public void testConcatMonos() {
        Flux<String> concat = MergeFluxes.concat(
                MonoGenerator.fooMono(),
                MonoGenerator.fooMono()
        );

        concat.subscribe(System.out::println);

        StepVerifier.create(concat)
                .expectNext("foo", "foo")
                .verifyComplete();
    }

}