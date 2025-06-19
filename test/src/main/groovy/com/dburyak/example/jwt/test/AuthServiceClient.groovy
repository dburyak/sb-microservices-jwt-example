package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import com.dburyak.example.jwt.api.auth.JwtLoginResponse
import com.dburyak.example.jwt.api.auth.JwtRefreshTokenRequest
import com.dburyak.example.jwt.api.auth.User
import com.dburyak.example.jwt.api.tenant.Tenant

import static BaseSpec.AUTH_SERVICE_URL
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_REFRESH
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_TOKEN
import static com.dburyak.example.jwt.api.common.Paths.PATH_USER_BY_UUID
import static com.dburyak.example.jwt.api.common.QueryParams.TENANT_UUID
import static org.springframework.http.MediaType.APPLICATION_JSON

class AuthServiceClient extends ServiceClient {

    AuthServiceClient(UUID tenantUuid = null, String jwtToken = null) {
        super(AUTH_SERVICE_URL, tenantUuid, jwtToken)
    }

    @Override
    protected AuthServiceClient createWith(UUID tenantUuid = null, String jwtToken = null) {
        new AuthServiceClient(tenantUuid, jwtToken)
    }

    JwtLoginResponse jwtLogin(JwtLoginRequest req) {
        rest.post()
                .uri { it.path(PATH_AUTH_JWT_TOKEN).build() }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(JwtLoginResponse)
    }

    JwtLoginResponse jwtRefresh(JwtRefreshTokenRequest req) {
        rest.post()
                .uri { it.path(PATH_AUTH_JWT_REFRESH).build() }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(JwtLoginResponse)
    }

    User findUserByUuid(UUID userUuid, UUID tenantUuid = null) {
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

    User findUserByUuid(UUID userUuid, Tenant tenant) {
        findUserByUuid(userUuid, tenant?.uuid)
    }
}
