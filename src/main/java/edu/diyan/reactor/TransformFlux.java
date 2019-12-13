package edu.diyan.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

class TransformFlux {

    public static void main(String[] args) throws InterruptedException {
        capitalizeUsernamesSlow().subscribe(System.out::println);
        waitFor(11);
    }

    // Execute slow operations parallel with flatMap
    static Flux<User> capitalizeUsernamesSlow() {
        return FluxGenerator.u1u2Flux()
                .flatMap(TransformFlux::asyncCapitalizeUser);
    }

    static Mono<User> asyncCapitalizeUser(User u) {
        // In case of a slow operation e.g. user transformation is done with a remote slow call
        return Mono.just(new User(u.getUsername().toUpperCase()))
                .delayElement(Duration.ofSeconds(10));
    }

    static Mono<User> capitalizeUsername() {
        return MonoGenerator.userMono()
                .map(u -> new User(u.getUsername().toUpperCase()));
    }

    private static void waitFor(int seconds) throws InterruptedException {
        for (int i = 0; i < seconds; i++) {
            if (i == 0) {
                System.out.print("Waiting..");
            } else {
                System.out.print("..");
            }

            Thread.sleep(1000);
        }
    }

}
