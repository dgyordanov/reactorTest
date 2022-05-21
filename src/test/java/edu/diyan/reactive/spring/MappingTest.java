package edu.diyan.reactive.spring;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class MappingTest {

    @Test
    public void mapTest() {
        var letters = Flux.just("a", "b", "c")
                // Create a Flux with modified elements by the function
                .map(String::toUpperCase);

        StepVerifier.create(letters)
                .expectNext("A", "B", "C")
                .verifyComplete();
    }

    @Test
    public void flatMapTest() {
        var flux = Flux.just(
                        Pair.of("1", 300),
                        Pair.of("2", 200),
                        Pair.of("3", 100)
                )
                // flatMap() merges the elements of the fluxes whenever they come
                .flatMap(e -> delayElement(e.getLeft(), e.getRight()));

        StepVerifier.create(flux)
                .expectNext("3", "2", "1")
                .verifyComplete();
    }

    @Test
    public void concatWithTest() {
        var flux = Flux.just(
                        Pair.of("1", 300),
                        Pair.of("2", 200),
                        Pair.of("3", 100)
                )
                // concatMap() merges the elements of the fluxes sequentially and of course is slower
                // The test runs as twice as longer compared to flatMapTest()
                .concatMap(e -> delayElement(e.getLeft(), e.getRight()));

        StepVerifier.create(flux)
                .expectNext("1", "2", "3")
                .verifyComplete();
    }

    /**
     * switchMap cancels the processing of previous flux once a new one comes in.
     * Switching from previous flux to the next one
     * Useful for situation like autocomplete.
     */
    @Test
    public void switchMapTest() {
        var autocompleteFlux = Flux.just("re", "rea", "reac", "react")
                .switchMap(e -> delayElement(e, 500));

        StepVerifier.create(autocompleteFlux)
                .expectNext("react")
                .verifyComplete();
    }

    private Mono<String> delayElement(String element, int delay) {
        return Mono.just(element).delayElement(Duration.ofMillis(delay));
    }

}
