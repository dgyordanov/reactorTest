package edu.diyan.reactor;

import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ErrorHandling {

    static Mono<String> stringMonoWithDefaultValueOnError(Mono<String> mono, String defaultValue) {
        return mono.onErrorReturn(defaultValue);
    }

    static Flux<String> stringFluxWithDefaultValueOnError(Flux<String> flux, String... defaultValues) {
        return flux.onErrorResume(e -> Flux.just(defaultValues));
    }

    static Flux<String> transformUsernames(Flux<User> flux) {
        return flux
                .map(u -> {
                    try {
                        return u.getUsernameUppercase();
                    } catch (UserException e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

}
