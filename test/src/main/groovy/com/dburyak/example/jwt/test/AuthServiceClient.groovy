package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import com.dburyak.example.jwt.api.auth.JwtLoginResponse
import com.dburyak.example.jwt.api.auth.JwtRefreshTokenRequest

import static BaseSpec.AUTH_SERVICE_URL
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_REFRESH
import static com.dburyak.example.jwt.api.auth.Paths.PATH_AUTH_JWT_TOKEN
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
}
