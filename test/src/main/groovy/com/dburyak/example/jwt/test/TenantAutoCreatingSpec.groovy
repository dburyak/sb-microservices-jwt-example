package com.dburyak.example.jwt.test

import spock.lang.Shared

/**
 * Spec that automatically creates tenant for testing, and deletes it at the cleanup.
 */
abstract class TenantAutoCreatingSpec extends SuperAdminLoggedInSpec {

    @Shared
    String tenantName = "test-tenant-${System.currentTimeMillis()}"

    @Shared
    UUID tenantUuid

    @Shared
    AuthServiceClient authServiceClientAdmin

    @Shared
    UserServiceClient userServiceClientAdmin

    @Shared
    TenantServiceClient tenantServiceClientAdmin

    def setupSpec() {

    }

    def cleanupSpec() {

    }
}
