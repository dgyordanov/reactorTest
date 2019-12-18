package edu.diyan.reactor;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class BlockingToReactive {

    public static void main(String[] args) {
        BlockingUserRepo blockingUserRepo = new BlockingUserRepo();

        // convert a non-reactive legacy repo to a reactive pipeline
        Flux<User> userFlux = Flux.defer(() -> Flux.fromIterable(blockingUserRepo.findAll()))
                .subscribeOn(Schedulers.elastic());

        userFlux.subscribe(System.out::println);
    }


}
