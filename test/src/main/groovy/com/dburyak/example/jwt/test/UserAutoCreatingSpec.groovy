package com.dburyak.example.jwt.test

import spock.lang.Shared

abstract class UserAutoCreatingSpec extends TenantAutoCreatingSpec {
    static final String USERNAME = System.getenv('USERNAME') ?: 'test-user'
    static final String CONTENT_MANAGER_USERNAME = System.getenv('CONTENT_MANAGER_USERNAME') ?: 'test-content-manager'
    static final String USER_MANAGER_USERNAME = System.getenv('USER_MANAGER_USERNAME') ?: 'test-user-manager'

    @Shared
    AuthServiceClient authServiceClient

    @Shared
    UserServiceClient userServiceClient

    @Shared
    TenantServiceClient tenantServiceClient

    def setupSpec() {

    }

    def cleanupSpec() {

    }
}
