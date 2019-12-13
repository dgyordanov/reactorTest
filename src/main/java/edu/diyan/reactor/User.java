package edu.diyan.reactor;

import lombok.Value;

@Value
class User {
    String username;

    String getUsernameUppercase() throws UserException {
        if (username == null) throw new UserException();
        return username.toUpperCase();
    }
}

class UserException extends Exception {
}