package edu.diyan.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ErrorHandlingTest {

    @Test
    public void testMonoDefaultValue() {
        String defaultValue = "default";

        Mono<String> errorMono = ErrorHandling.stringMonoWithDefaultValueOnError(MonoGenerator.errorMono(), defaultValue);

        StepVerifier.create(errorMono)
                .expectNext(defaultValue)
                .verifyComplete();

        Mono<String> mono = ErrorHandling.stringMonoWithDefaultValueOnError(MonoGenerator.fooMono(), defaultValue);
        StepVerifier.create(mono)
                .expectNext("foo")
                .verifyComplete();
    }

    @Test
    public void testFluxDefaultValue() {
        Flux<String> errorFlux = ErrorHandling.stringFluxWithDefaultValueOnError(FluxGenerator.errorFlux(), "v1", "v2");

        StepVerifier.create(errorFlux)
                .expectNext("v1", "v2")
                .verifyComplete();
    }

    @Test
    public void testException() {
        User user1 = new User("1");
        Flux<String> fluxWithException = ErrorHandling.transformUsernames(
                Flux.just(user1, new User(null))
        );

        StepVerifier.create(fluxWithException)
                .expectNext(user1.getUsername())
                .verifyError(UserException.class);
    }

}