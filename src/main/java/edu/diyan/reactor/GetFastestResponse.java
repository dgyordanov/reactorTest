package edu.diyan.reactor;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

public class GetFastestResponse {

    public static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        Mono.first(
                findUserInMongo(),
                findUserInMySQL()
        )
                .subscribe(System.out::println);

        Thread.sleep(5000);
    }

    static Mono<User> findUserInMongo() {
        return Mono.just(new User("u1Mongo"))
                .delayElement(Duration.ofSeconds(random.nextInt(5)));
    }

    static Mono<User> findUserInMySQL() {
        return Mono.just(new User("u1MySQL"))
                .delayElement(Duration.ofSeconds(random.nextInt(5)));
    }

}
