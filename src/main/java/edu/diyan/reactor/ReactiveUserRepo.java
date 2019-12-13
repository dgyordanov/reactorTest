package edu.diyan.reactor;

import reactor.core.publisher.Flux;

class ReactiveUserRepo {

    Flux<User> findAll() {
        return Flux.just(
                new User("u1"),
                new User("u2"),
                new User("u3"),
                new User("u4"),
                new User("u5")
        );
    }


}
