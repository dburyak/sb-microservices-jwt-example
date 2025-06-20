package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.tenant.Tenant
import com.dburyak.example.jwt.api.user.User
import com.dburyak.example.jwt.api.user.UserWithRoles

import static BaseSpec.USER_SERVICE_URL
import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_BY_UUID
import static com.dburyak.example.jwt.api.common.Paths.USERS
import static com.dburyak.example.jwt.api.common.QueryParams.DEVICE_ID
import static com.dburyak.example.jwt.api.common.QueryParams.TENANT_UUID
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT_BY_USERNAME
import static com.dburyak.example.jwt.api.user.Paths.PATH_USER_MANAGEMENT_BY_UUID
import static com.dburyak.example.jwt.api.user.QueryParams.REGISTRATION_CODE
import static org.springframework.http.MediaType.APPLICATION_JSON

class UserServiceClient extends ServiceClient {

    UserServiceClient(UUID tenantUuid = null, String jwtToken = null) {
        super(USER_SERVICE_URL, tenantUuid, jwtToken)
    }

    @Override
    protected UserServiceClient createWith(UUID tenantUuid = null, String jwtToken = null) {
        new UserServiceClient(tenantUuid, jwtToken)
    }

    User register(User req, String deviceId, String otpCode) {
        rest.post()
                .uri {
                    it.path(USERS)
                            .queryParam(DEVICE_ID, deviceId)
                            .queryParam(REGISTRATION_CODE, otpCode)
                            .build()
                }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(User)
    }

    User findByUuid(UUID userUuid, UUID tenantUuid = null) {
        rest.get()
                .uri {
                    def builder = it.path(PATH_USER_BY_UUID)
                    if (tenantUuid) {
                        builder = builder.queryParam(TENANT_UUID, tenantUuid)
                    }
                    builder.build(userUuid)
                }
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(User)
    }

    User findByUuid(UUID userUuid, Tenant tenant) {
        findByUuid(userUuid, tenant?.uuid)
    }

    User findByUsernameByManager(String username, UUID tenantUuid = null) {
        rest.get()
                .uri {
                    def builder = it.path(PATH_USER_MANAGEMENT_BY_USERNAME)
                    if (tenantUuid) {
                        builder = builder.queryParam(TENANT_UUID, tenantUuid)
                    }
                    builder.build(username)
                }
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(User)
    }

    User findByUsernameByManager(String username, Tenant tenant) {
        findByUsernameByManager(username, tenant?.uuid)
    }

    User createByManager(UserWithRoles req, UUID tenantUuid = null) {
        rest.post()
                .uri {
                    def builder = it.path(PATH_USER_MANAGEMENT)
                    if (tenantUuid) {
                        builder = builder.queryParam(TENANT_UUID, tenantUuid)
                    }
                    builder.build()
                }
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(User)
    }

    User createByManager(UserWithRoles req, Tenant tenant) {
        createByManager(req, tenant?.uuid)
    }

    void deleteByUsernameByManager(String username, UUID tenantUuid = null) {
        rest.delete()
                .uri {
                    def builder = it.path(PATH_USER_MANAGEMENT_BY_USERNAME)
                    if (tenantUuid) {
                        builder = builder.queryParam(TENANT_UUID, tenantUuid)
                    }
                    builder.build(username)
                }
                .retrieve()
                .body(Void)
    }

    void deleteByUuidByManager(UUID userUuid, UUID tenantUuid = null) {
        rest.delete()
                .uri {
                    def builder = it.path(PATH_USER_MANAGEMENT_BY_UUID)
                    if (tenantUuid) {
                        builder = builder.queryParam(TENANT_UUID, tenantUuid)
                    }
                    builder.build(userUuid)
                }
                .retrieve()
                .body(Void)
    }

    void deleteByUuidByManager(UUID userUuid, Tenant tenant) {
        deleteByUuidByManager(userUuid, tenant?.uuid)
    }
}
