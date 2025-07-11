package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.tenant.Tenant
import spock.lang.Shared

/**
 * Spec that automatically creates tenant for testing, and deletes it at the cleanup.
 */
abstract class TenantAutoCreatingSpec extends SuperAdminLoggedInSpec {
    static final String TENANT_DESCRIPTION = 'test tenant created by tests'

    @Shared
    String tenantName = "test-tenant-${rndString.next(5, RND_NAME_CHARS)}"

    @Shared
    String adminEmail = "admin.${rndString.next(5, RND_NAME_CHARS)}@$TEST_DOMAIN"

    @Shared
    UUID tenantUuid

    @Shared
    Tenant tenant

    def setupSpec() {
        def createTenantReq = Tenant.builder()
                .name(tenantName)
                .description(TENANT_DESCRIPTION)
                .adminEmail(adminEmail)
                .build()
        tenant = tenantServiceClientSA.create(createTenantReq)
        tenantUuid = tenant.uuid
    }

    def cleanupSpec() {
        tenantServiceClientSA.deleteByUuid(tenantUuid)
    }
}
