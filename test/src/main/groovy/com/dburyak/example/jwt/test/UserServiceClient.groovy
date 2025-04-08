package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.user.User

import static com.dburyak.example.jwt.api.user.Paths.USERS_ROOT
import static com.dburyak.example.jwt.test.JwtExampleSpec.USER_SERVICE_URL
import static org.springframework.http.MediaType.APPLICATION_JSON

class UserServiceClient extends ServiceClient {

    UserServiceClient(String tenantId = null, String jwtToken = null) {
        super(USER_SERVICE_URL, tenantId, jwtToken)
    }

    User register(User req) {
        rest.post()
                .uri { it.path(USERS_ROOT) }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(User)
    }
}
