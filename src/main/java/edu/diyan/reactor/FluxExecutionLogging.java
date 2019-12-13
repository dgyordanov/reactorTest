package edu.diyan.reactor;

import reactor.core.publisher.Flux;

public class FluxExecutionLogging {

    public static void main(String[] args) {
        Flux<User> usersWithLog1 = new ReactiveUserRepo()
                .findAll()
                .log();

        Flux<User> usersWithLog2 = new ReactiveUserRepo().findAll()
                .doOnSubscribe(subscription -> System.out.println("Starring:"))
                .doOnNext(u -> System.out.println(u.getUsername()))
                .doOnComplete(() -> System.out.println("The end!"));

        usersWithLog1.subscribe(System.out::println);
        usersWithLog2.subscribe(System.out::println);
    }

}
