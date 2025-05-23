package com.dburyak.example.jwt.lib.err;

import lombok.Getter;

public class AlreadyExistsException extends RuntimeException {

    @Getter
    private final String resource;

    public AlreadyExistsException(String resource) {
        super("already exists: " + resource);
        this.resource = resource;
    }
}
