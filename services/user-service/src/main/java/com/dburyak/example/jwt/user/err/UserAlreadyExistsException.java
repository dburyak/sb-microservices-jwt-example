package com.dburyak.example.jwt.user.err;

import com.dburyak.example.jwt.lib.err.AlreadyExistsException;

import java.util.UUID;

public class UserAlreadyExistsException extends AlreadyExistsException {

    public UserAlreadyExistsException(UUID userUuid) {
        super(resourceName(userUuid));
    }

    public UserAlreadyExistsException(String email) {
        super(resourceName(email));
    }

    private static String resourceName(UUID userUuid) {
        return "User(userUuid=%s)".formatted(userUuid);
    }

    private static String resourceName(String email) {
        return "User(email=%s)".formatted(email);
    }
}
