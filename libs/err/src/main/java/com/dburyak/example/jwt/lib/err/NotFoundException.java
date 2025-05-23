package com.dburyak.example.jwt.lib.err;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

    @Getter
    private final String resource;

    public NotFoundException(String resource) {
        super("not found: " + resource);
        this.resource = resource;
    }
}
