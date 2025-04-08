package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.tenant.Tenant

class TenantSpec extends JwtExampleSpec {

    def 'create tenant'() {
        given:
        def tenantId = "test-tenant-${System.currentTimeMillis()}"
        def tenantClient = new TenantServiceClient(tenantId)

        when:
        def resp = tenantClient.create(Tenant.builder()
                .tenantId(tenantId)
                .build())

        then:
        resp.tenantId == tenantId

        and:
        tenantClient.tenantExists(tenantId)

        cleanup:
        tenantClient.delete(tenantId)
    }
}
