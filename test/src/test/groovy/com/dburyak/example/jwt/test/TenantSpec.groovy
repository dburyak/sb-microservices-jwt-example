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

    def 'create tenant - creates tenant successfully'() {
        when: 'new tenant created'
        tenant = tenantServiceClientSA.create(createTenantReq)

        then: 'create response is successful'
        tenant.name == tenantName

        and: 'we can retrieve newly created tenant'
        def getTenantResp = tenantServiceClientSA.getByName(tenantName)
        getTenantResp != null
        getTenantResp.uuid == tenant.uuid

        cleanup:
        tenantServiceClientSA.deleteByName(tenantName)
    }

    def 'create tenant - creates tenant admin user'() {
        when: 'new tenant created'
        tenant = tenantServiceClientSA.create(createTenantReq)
        sleep 1_000 // wait for async msg being published, password hashed, saved to db

        then: 'admin user for new tenant is created'
        def adminUser = userServiceClientSA.findByUsername(ADMIN_USERNAME, tenant)
        adminUser.username == ADMIN_USERNAME

        and: 'admin user has admin role'
        def adminAuthUser = authServiceClientSA.findUserByUuid(adminUser.uuid, tenant)
        adminAuthUser.roles.contains(ADMIN_ROLE)

        cleanup:
        tenantServiceClientSA.deleteByName(tenantName)
    }

    def 'delete tenant - deletes tenant successfully'() {
        given: 'tenant created'

        when: 'tenant is deleted'
        tenantServiceClientSA.deleteByName(tenantName)

        then: 'tenant is not found anymore'
        def getTenantResp = tenantServiceClientSA.getByName(tenantName)
        getTenantResp == null

        and: 'admin user for deleted tenant is not found anymore'
        def adminUser = userServiceClientSA.findByUsername(ADMIN_USERNAME, tenant)
        adminUser == null

        and: 'admin user for deleted tenant is not found in auth service'
        def adminAuthUser = authServiceClientSA.findUserByUuid(adminUser?.uuid, tenant)
        adminAuthUser == null
    }

    def 'delete tenant - deletes all users of the tenant'() {
        given: 'tenant created with admin user'

        when: 'tenant is deleted'
        tenantServiceClientSA.deleteByName(tenantName)

        then: 'all users of the tenant are deleted'
        def allUsers = userServiceClientSA.findAll(tenant)
        allUsers.isEmpty()

        and: 'all auth users of the tenant are deleted'
        def allAuthUsers = authServiceClientSA.findAll(tenant)
        allAuthUsers.isEmpty()
    }
}
