package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import com.dburyak.example.jwt.api.auth.JwtLoginResponse
import com.dburyak.example.jwt.api.auth.JwtRefreshTokenRequest

import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_REFRESH
import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_ROOT
import static com.dburyak.example.jwt.api.auth.Paths.AUTH_JWT_TOKEN
import static com.dburyak.example.jwt.test.JwtExampleSpec.AUTH_SERVICE_URL
import static org.springframework.http.MediaType.APPLICATION_JSON

class AuthServiceClient extends ServiceClient {

    AuthServiceClient(String tenantId = null, String jwtToken = null) {
        super(AUTH_SERVICE_URL, tenantId, jwtToken)
    }

    JwtLoginResponse jwtLogin(JwtLoginRequest req) {
        rest.post()
                .uri { it.path(AUTH_JWT_ROOT + AUTH_JWT_TOKEN) }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(JwtLoginResponse)
    }

    JwtLoginResponse jwtRefresh(JwtRefreshTokenRequest req) {
        rest.post()
                .uri { it.path(AUTH_JWT_ROOT + AUTH_JWT_REFRESH) }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(JwtLoginResponse)
    }
}
