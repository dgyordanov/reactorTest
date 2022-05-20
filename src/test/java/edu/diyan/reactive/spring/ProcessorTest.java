package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.test.StepVerifier;

/**
 * Processors satisfy both Publisher and Subscriber interfaces.
 * They are useful to adapt on type to another
 */
public class ProcessorTest {

    @Test
    public void emitterProcessorTest() {
        // Acts like a pump/queue allowing one end to pump data into it and the other to consume it
        EmitterProcessor<Integer> emitterProcessor = EmitterProcessor.create();
        produce(emitterProcessor.sink());
        consume(emitterProcessor);
    }

    private void produce(FluxSink<Integer> sink) {
        sink.next(1);
        sink.next(2);
        sink.next(3);
        sink.complete();
    }

    private void consume(Flux<Integer> emitterProcessor) {
        StepVerifier.create(emitterProcessor)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .verifyComplete();
    }

    @Test
    public void replyProcessorTest() {
        // Reply last X events events to different subscriber
        int historySize = 2;
        boolean unbounded = false;
        ReplayProcessor<Integer> emitterProcessor = ReplayProcessor.create(historySize, unbounded);
        produce(emitterProcessor.sink());
        consumeReply(emitterProcessor);
    }

    private void consumeReply(Flux<Integer> emitterProcessor) {
        // try with 5 subscribers
        for (int i = 0; i < 5; i++) {
            StepVerifier.create(emitterProcessor)
                    .expectNext(2)
                    .expectNext(3)
                    .verifyComplete();
        }
    }

}
