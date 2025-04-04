package com.dburyak.example.jwt.lib.err;

public class NotFoundException extends RuntimeException {
    private final String resource;

    public NotFoundException(String resource) {
        super("not found: " + resource);
        this.resource = resource;
    }
}
