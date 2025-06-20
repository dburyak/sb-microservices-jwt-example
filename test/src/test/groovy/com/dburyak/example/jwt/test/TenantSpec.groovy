package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.common.ExternalId
import com.dburyak.example.jwt.api.tenant.Tenant
import com.dburyak.example.jwt.api.user.ContactInfo
import com.dburyak.example.jwt.api.user.UserWithRoles

class TenantSpec extends SuperAdminLoggedInSpec {
    static final String TENANT_DESCRIPTION = 'test tenant created by tests'

    def tenantName = "test-tenant-${System.currentTimeMillis()}-${UUID.randomUUID()}"
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

    def 'create tenant - creates tenant successfully'() {
        given: 'new tenant created'

        expect: 'create response is successful'
        tenant.name == tenantName

        and: 'we can retrieve newly created tenant'
        def getTenantResp = tenantServiceClientSA.getByName(tenantName)
        getTenantResp.uuid == tenant.uuid

        cleanup:
        tenantServiceClientSA.deleteByName(tenantName)
    }

    def 'create tenant - creates tenant admin user'() {
        given: 'new tenant created'
        sleep 1_000 // wait for async msg being published, password hashed, saved to db

        expect: 'admin user for new tenant is created'
        def adminUser = userServiceClientSA.findByUsernameByManager(ADMIN_USERNAME, tenant)
        adminUser.username == ADMIN_USERNAME

        and: 'admin user has admin role'
        def adminAuthUser = authServiceClientSA.findUserByUuid(adminUser.uuid, tenant)
        adminAuthUser.roles.contains(ADMIN_ROLE)

        cleanup:
        tenantServiceClientSA.deleteByName(tenantName)
    }

    def 'delete tenant - deletes tenant successfully'() {
        given: 'tenant created and can be retrieved'

        when: 'delete tenant'
        tenantServiceClientSA.deleteByName(tenantName)

        and: 'attempt to retrieve deleted tenant'
        tenantServiceClientSA.getByName(tenantName)

        then: 'tenant is not found anymore'
        def err = thrown(Exception)
        err.message.containsIgnoreCase('not found')
        err.message.containsIgnoreCase(tenantName)
    }

    def 'delete tenant - deletes admin user of the tenant'() {
        given: 'tenant created with admin user'
        def adminUser = userServiceClientSA.findByUsernameByManager(ADMIN_USERNAME, tenant)
        def adminUserUuid = adminUser.uuid

        when: 'delete tenant'
        tenantServiceClientSA.deleteByName(tenantName)
        sleep 1_000 // wait for async msg being processed

        and: 'attempt to retrieve admin user for the deleted tenant'
        userServiceClientSA.findByUsernameByManager(ADMIN_USERNAME, tenant)

        then: 'admin user for the deleted tenant is not found anymore'
        def err = thrown(Exception)
        err.message.containsIgnoreCase('not found')
        err.message.containsIgnoreCase(ADMIN_USERNAME)

        when: 'attempt to retrieve admin user for the deleted tenant in auth service'
        authServiceClientSA.findUserByUuid(adminUserUuid, tenant)

        then: 'admin user for the deleted tenant is not found in auth service'
        def authErr = thrown(Exception)
        authErr.message.containsIgnoreCase('not found')
        authErr.message.containsIgnoreCase(adminUserUuid.toString())
    }

    def 'delete tenant - deletes all users of the tenant'() {
        given: 'tenant created with several users'
        def user1Name = "user1-$tenantName"
        def user1Email = "$user1Name@test.jwt.example.dburyak.com"
        def user1 = userServiceClientSA.createByManager(UserWithRoles.builder()
                .roles(Set.of('user'))
                .externalId(new ExternalId(user1Email))
                .username(user1Name)
                .password('test-user1-password123')
                .displayName(user1Name)
                .profileIcon('user')
                .contactInfo(new ContactInfo(user1Email))
                .build(), tenant)
        def user2Name = "user2-$tenantName"
        def user2Email = "$user2Name@test.jwt.example.dburyak.com"
        def user2 = userServiceClientSA.createByManager(UserWithRoles.builder()
                .roles(Set.of('user'))
                .externalId(new ExternalId(user2Email))
                .username(user2Name)
                .password('test-user2-password123')
                .displayName(user2Name)
                .profileIcon('user')
                .contactInfo(new ContactInfo(user2Email))
                .build(), tenant)

        when: 'tenant is deleted'
        tenantServiceClientSA.deleteByName(tenantName)

        and: 'try to retrieve users of the deleted tenant'
        userServiceClientSA.findByUsernameByManager(user1.username, tenant)

        then: 'all users of the tenant are deleted'
        def err = thrown(Exception)
        err.message.containsIgnoreCase('not found')
        err.message.containsIgnoreCase(user1.username)

        when:
        userServiceClientSA.findByUsernameByManager(user2.username, tenant)

        then: 'all users of the tenant are deleted'
        def err2 = thrown(Exception)
        err2.message.containsIgnoreCase('not found')
        err2.message.containsIgnoreCase(user2.username)
    }
}
