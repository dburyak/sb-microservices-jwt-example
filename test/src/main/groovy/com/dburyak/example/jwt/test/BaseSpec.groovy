package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import org.apache.commons.lang3.RandomStringUtils
import spock.lang.Shared
import spock.lang.Specification

/**
 * Base spec with constants only, no any setup/cleanup.
 */
abstract class BaseSpec extends Specification {
    static final String SA_PASSWORD = System.getenv('JWT_EXAMPLE_SA_PASSWORD')

    static {
        if (!SA_PASSWORD) {
            throw new IllegalStateException('JWT_EXAMPLE_SA_PASSWORD environment variable must be set')
        }
    }

    static final String SA_TENANT_NAME = System.getenv('SA_TENANT_NAME') ?: 'system-admin'
    static final String SA_USERNAME = System.getenv('SA_USERNAME') ?: 'sa'
    static final String SA_DEVICE_ID = System.getenv('SA_DEVICE_ID') ?: 'sa-test-device'
    static final String ADMIN_USERNAME = 'admin'
    static final String ADMIN_DEVICE_ID = System.getenv('ADMIN_DEVICE_ID') ?: 'admin-test-device'

    static final String TEST_DOMAIN = System.getenv('TEST_DOMAIN') ?: 'test.jwt.example.dburyak.com'
    static final String SA_ROLE = 'sa'
    static final String ADMIN_ROLE = 'adm'
    static final String CONTENT_MANAGER_ROLE = 'cmg'
    static final String USER_MANAGER_ROLE = 'umg'
    static final String USER_ROLE = 'usr'
    static final String SERVICE_ROLE = 'srv'
    static final String RND_NAME_CHARS = 'abcdefghijklmnopqrstuvwxyz0123456789'

    static final String AUTH_SERVICE_URL = System.getenv('AUTH_SERVICE_URL') ?: 'http://localhost:8080'
    static final String USER_SERVICE_URL = System.getenv('USER_SERVICE_URL') ?: 'http://localhost:8081'
    static final String TENANT_SERVICE_URL = System.getenv('TENANT_SERVICE_URL') ?: 'http://localhost:8082'
    static final String OTP_SERVICE_URL = System.getenv('OTP_SERVICE_URL') ?: 'http://localhost:8084'

    static final JwtLoginRequest SA_LOGIN_REQUEST = JwtLoginRequest.builder()
            .username(SA_USERNAME)
            .password(SA_PASSWORD)
            .deviceId(SA_DEVICE_ID)
            .build()

    @Shared
    RandomStringUtils rndString = RandomStringUtils.secure()
}
