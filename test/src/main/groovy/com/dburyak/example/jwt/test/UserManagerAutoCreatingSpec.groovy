package com.dburyak.example.jwt.test


import com.dburyak.example.jwt.api.common.ExternalId
import com.dburyak.example.jwt.api.user.ContactInfo
import com.dburyak.example.jwt.api.user.UserWithRoles
import spock.lang.Shared

/**
 * Base spec that creates/deletes tenant for testing and creates/deletes user-manager user under this tenant.
 */
abstract class UserManagerAutoCreatingSpec extends TenantAutoCreatingSpec {

    @Shared
    String umEmail = "um.${System.currentTimeMillis()}@test.jwt.example.dburyak.com"

    @Shared
    String umUsername = "um-$tenantName"

    @Shared
    String umPassword = rndString.nextGraph(25)

    @Shared
    UUID umUserUuid

    @Shared
    String umDeviceId = 'um-test-device-' + System.currentTimeMillis()

    def setupSpec() {
        def umUser = userServiceClientSA.createByManager(UserWithRoles.builder()
                .externalId(new ExternalId(umEmail))
                .username(umUsername)
                .password(umPassword)
                .displayName(umUsername)
                .profileIcon('user-manager')
                .contactInfo(new ContactInfo(umEmail))
                .roles(Set.of('umg'))
                .build(), tenant)
        umUserUuid = umUser.uuid
    }

    def cleanupSpec() {
        userServiceClientSA.deleteByUuidByManager(umUserUuid, tenant)
    }
}
