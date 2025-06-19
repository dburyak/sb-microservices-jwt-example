package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.tenant.Tenant

class TenantSpec extends SuperAdminLoggedInSpec {
    static final String TENANT_DESCRIPTION = 'test tenant created by tests'

    def tenantName = "test-tenant-${System.currentTimeMillis()}"
    def adminEmail = "admin-$tenantName@test.jwt.example.dburyak.com"
    def createTenantReq = Tenant.builder()
            .name(tenantName)
            .description(TENANT_DESCRIPTION)
            .adminEmail(adminEmail)
            .build()
    Tenant tenant

    def setup() {
        tenant = tenantServiceClientSA.create(createTenantReq)
    }

    def cleanup() {
        tenantServiceClientSA.deleteByName(tenantName)
    }

    def 'create tenant - creates tenant successfully'() {
        given: 'new tenant created'

        expect: 'create response is successful'
        tenant.name == tenantName

        and: 'we can retrieve newly created tenant'
        def getTenantResp = tenantServiceClientSA.getByName(tenantName)
        getTenantResp != null
        getTenantResp.uuid == tenant.uuid
    }

    def 'create tenant - creates tenant admin user'() {
        when: 'new tenant created'
        sleep 1_000 // wait for async msg being published, password hashed, saved to db

        then: 'admin user for new tenant is created'
        def adminUser = userServiceClientSA.findByUsername(ADMIN_USERNAME, tenant)
        adminUser.username == ADMIN_USERNAME

        and: 'admin user has admin role'
        def adminAuthUser = authServiceClientSA.findUserByUuid(adminUser.uuid, tenant)
        adminAuthUser.roles.contains(ADMIN_ROLE)
    }
}
