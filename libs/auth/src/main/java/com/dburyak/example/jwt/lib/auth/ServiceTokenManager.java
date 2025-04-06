package com.dburyak.example.jwt.lib.auth;

import com.dburyak.example.jwt.lib.auth.cfg.JwtAuthProperties;

public class ServiceTokenManager {
    private final JwtGenerator jwtGenerator;

    public ServiceTokenManager(JwtAuthProperties props, JwtGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }
}
