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

    def 'create tenant - creates tenant successfully'() {
        when: 'create tenant'
        def tenantResp = tenantServiceClientSA.create(createTenantReq)

        then: 'create response is successful'
        tenantResp.name == tenantName

        and: 'we can retrieve newly created tenant'
        def getTenantResp = tenantServiceClientSA.getByName(tenantName)
        getTenantResp != null
        getTenantResp.uuid == tenantResp.uuid

        cleanup:
        tenantServiceClientSA.deleteByName(tenantName)
    }

    def 'create tenant - creates tenant admin user'() {
        when: 'create tenant'
        def tenantResp = tenantServiceClientSA.create(createTenantReq)
        sleep 2_000 // wait for async msg being published, password hashed, saved to db

        and: 'fetch admin user of the newly created tenant'
        // TODO: implement here ..............................
        userServiceClientSA

        then: 'admin user exists with correct data'
        100 == 100 // TODO: implement here ..............................
    }
}
