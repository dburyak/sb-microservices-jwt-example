package com.dburyak.example.jwt.auth.err;

import com.dburyak.example.jwt.lib.err.NotFoundException;

import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(UUID userUuid) {
        super("User(uuid=%s)".formatted(userUuid));
    }

    public UserNotFoundException(String username) {
        super("User(username=%s)".formatted(username));
    }
}
