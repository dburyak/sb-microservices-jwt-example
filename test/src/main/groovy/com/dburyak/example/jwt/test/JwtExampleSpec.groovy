package com.dburyak.example.jwt.test

import spock.lang.Shared
import spock.lang.Specification

abstract class JwtExampleSpec extends Specification {
    static final String AUTH_SERVICE_URL = System.getenv('AUTH_SERVICE_URL') ?: 'http://localhost:8080'
    static final String USER_SERVICE_URL = System.getenv('USER_SERVICE_URL') ?: 'http://localhost:8081'
    static final String TENANT_SERVICE_URL = System.getenv('TENANT_SERVICE_URL') ?: 'http://localhost:8082'
    static final String TENANT_NAME = System.getenv('TENANT_NAME') ?: 'test'
    static final UUID TENANT_UUID =
            UUID.fromString(System.getenv('TENANT_UUID') ?: '1f3af2c3-a4f7-43c4-9df0-b877bfe7d92d')
    static final String USERNAME = System.getenv('USERNAME') ?: 'test-user'
    static final String SA_USERNAME = System.getenv('SA_USERNAME') ?: 'test-super-admin'
    static final String ADMIN_USERNAME = System.getenv('ADMIN_USERNAME') ?: 'test-admin'
    static final String CONTENT_MANAGER_USERNAME = System.getenv('CONTENT_MANAGER_USERNAME') ?: 'test-content-manager'
    static final String USER_MANAGER_USERNAME = System.getenv('USER_MANAGER_USERNAME') ?: 'test-user-manager'

    @Shared
    AuthServiceClient authClient = new AuthServiceClient()

    @Shared
    UserServiceClient userClient = new UserServiceClient()

    @Shared
    TenantServiceClient tenantClient = new TenantServiceClient()
}
