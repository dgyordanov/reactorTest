package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Flux.create(Consumer) is a great way to adapt not reactive code to reactive
 */
public class NotReactiveToReactiveTest {

    @Test
    public void testFluxCreationFromNotReactiveCode() {
        final TemperatureEmitter temperatureEmitter = new TemperatureEmitter();

        Flux<Integer> temperateFlux = Flux.create(temperatureEmitter);

        StepVerifier.create(temperateFlux
                .doFinally(signal -> temperatureEmitter.shutdown()))
                .expectNextCount(10)
                .verifyComplete();
    }

    public static class TemperatureEmitter implements Consumer<FluxSink<Integer>> {

        // Spin up a background thread to connect to external APIs and generate elements
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        @Override
        public void accept(FluxSink<Integer> sink) {
            executorService.submit(() -> {
                IntStream.range(0, 10)
                        .forEach(
                                // simulation of connecting to an external API
                                i -> sink.next(ThreadLocalRandom.current().nextInt(3, 32))
                        );
                sink.complete();
            });
        }

        public void shutdown() {
            executorService.shutdown();
        }
    }

}
