package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.user.User

import static com.dburyak.example.jwt.api.user.Paths.USERS
import static BaseSpec.USER_SERVICE_URL
import static org.springframework.http.MediaType.APPLICATION_JSON

class UserServiceClient extends ServiceClient {

    UserServiceClient(UUID tenantUuid = null, String jwtToken = null) {
        super(USER_SERVICE_URL, tenantUuid, jwtToken)
    }

    @Override
    protected UserServiceClient createWith(UUID tenantUuid = null, String jwtToken = null) {
        new UserServiceClient(tenantUuid, jwtToken)
    }

    User register(User req) {
        rest.post()
                .uri { it.path(USERS) }
                .contentType(APPLICATION_JSON)
                .body(req)
                .retrieve()
                .body(User)
    }
}
