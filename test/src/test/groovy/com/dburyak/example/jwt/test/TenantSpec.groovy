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
        when:
        def resp = tenantServiceClientSA.create(createTenantReq)

        then:
        resp.name == tenantName

        and:
        tenantServiceClientSA.tenantExistsByName(tenantName)

        cleanup:
        tenantServiceClientSA.delete(tenantName)
    }

    def 'create tenant - creates tenant admin'() {

    }
}
