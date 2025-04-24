package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
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
        def tenantResp = tenantServiceClientSA.create(createTenantReq)

        then:
        tenantResp.name == tenantName

        and:
        tenantServiceClientSA.tenantExistsByName(tenantName)

        cleanup:
        tenantServiceClientSA.delete(tenantName)
    }

    def 'create tenant - creates tenant admin user'() {
        when:
        def tenantResp = tenantServiceClientSA.create(createTenantReq)
        sleep 1_000 // wait for async msg being published, password hashed, saved to db

        and:
        def tenantUuid = tenantResp.uuid
        def authServiceClientAdmin = new AuthServiceClient(tenantUuid)
        def loginAdminResp = authServiceClientAdmin.jwtLogin(JwtLoginRequest.builder()
                .username(adminEmail)
                .password(createTenantReq.password)
                .build())

        then:
        loginAdminResp.accessToken
    }
}
