package edu.diyan.reactive.spring;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * handle() can be use to take a full control on the emitting of element in case of complex logic
 */
public class HandleTest {

    @Test
    public void handleTest() {
        var flux = Flux.just(1, 2, 3, 4, 5)
                .handle((e, sink) -> {
                    if (e < 4) {
                        sink.next(e);
                        return;
                    }
                    if (e == 4) {
                        sink.error(new IllegalArgumentException());
                        return;
                    }
                    sink.complete();
                });

        StepVerifier.create(flux)
                .expectNext(1, 2, 3)
                .expectError(IllegalArgumentException.class)
                .verify();

    }

}
