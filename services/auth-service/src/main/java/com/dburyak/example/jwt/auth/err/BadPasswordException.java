package com.dburyak.example.jwt.auth.err;

public class BadPasswordException extends RuntimeException {

    public BadPasswordException(String description) {
        super("bad password: " + description);
    }
}
