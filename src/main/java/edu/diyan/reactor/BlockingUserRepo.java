package edu.diyan.reactor;

import java.util.List;

public class BlockingUserRepo {

    List<User> findAll() {
        return List.of(
                new User("u1"),
                new User("u2"),
                new User("u3"),
                new User("u4"),
                new User("u5"),
                new User("u6"),
                new User("u7"),
                new User("u8")
        );
    }
}
