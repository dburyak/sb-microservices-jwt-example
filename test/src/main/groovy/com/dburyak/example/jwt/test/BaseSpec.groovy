package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import spock.lang.Specification

/**
 * Base spec with constants only, no any setup/cleanup.
 */
abstract class BaseSpec extends Specification {
    static final String SA_PASSWORD = System.getenv('SA_PASSWORD')

    static {
        if (!SA_PASSWORD) {
            throw new IllegalStateException('SA_PASSWORD environment variable must be set')
        }
    }

    static final String SA_USERNAME = System.getenv('SA_USERNAME') ?: 'sa'
    static final String SA_DEVICE_ID = System.getenv('SA_DEVICE_ID') ?: 'sa-test-device'

    static final String AUTH_SERVICE_URL = System.getenv('AUTH_SERVICE_URL') ?: 'http://localhost:8080'
    static final String USER_SERVICE_URL = System.getenv('USER_SERVICE_URL') ?: 'http://localhost:8081'
    static final String TENANT_SERVICE_URL = System.getenv('TENANT_SERVICE_URL') ?: 'http://localhost:8082'

    static final JwtLoginRequest SA_LOGIN_REQUEST = JwtLoginRequest.builder()
            .username(SA_USERNAME)
            .password(SA_PASSWORD)
            .deviceId(SA_DEVICE_ID)
            .build()
}
