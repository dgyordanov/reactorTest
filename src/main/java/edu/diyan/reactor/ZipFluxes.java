package edu.diyan.reactor;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;

public class ZipFluxes {
    public static void main(String[] args) {
        // the zipped flux completes when on of the provided fluxes completes
        Flux.zip(
                slowUsersFlux(),
                instantFlux(),
                Flux.range(1, 6)
        )
                .map(t -> String.format("%s User %s interacted at %s ", t.getT3(), t.getT1(), t.getT2()))
                .subscribe(System.out::println);
    }

    private static Flux<User> slowUsersFlux() {
        return Flux.just(
                new User("u1"),
                new User("u2"),
                new User("u3")
        );
    }

    private static Flux<Instant> instantFlux() {
        return Flux.just(
                Instant.now().minus(Duration.ofSeconds(5)),
                Instant.now().minus(Duration.ofSeconds(3)),
                Instant.now().minus(Duration.ofSeconds(2)),
                Instant.now()
        );
    }

}
