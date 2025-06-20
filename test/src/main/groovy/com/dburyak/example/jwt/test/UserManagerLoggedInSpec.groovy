package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import spock.lang.Shared

/**
 * Base spec that creates/deletes tenant for testing, creates/deletes user-manager user under this tenant, and logs in
 * as this user-manager user.
 */
abstract class UserManagerLoggedInSpec extends UserManagerAutoCreatingSpec {

    @Shared
    String umAuthToken

    @Shared
    AuthServiceClient authServiceClientUM

    @Shared
    UserServiceClient userServiceClientUM

    @Shared
    TenantServiceClient tenantServiceClientUM

    @Shared
    OtpServiceClient otpServiceClientUM

    def setupSpec() {
        def authServiceClient = new AuthServiceClient(tenantUuid)
        def loginResp = authServiceClient.jwtLogin(JwtLoginRequest.builder()
                .username(umUsername)
                .password(umPassword)
                .deviceId(umDeviceId)
                .build())
        assert loginResp.accessToken
        umAuthToken = loginResp.accessToken
        authServiceClientUM = new AuthServiceClient(tenantUuid, umAuthToken)
        userServiceClientUM = new UserServiceClient(tenantUuid, umAuthToken)
        tenantServiceClientUM = new TenantServiceClient(tenantUuid, umAuthToken)
        otpServiceClientUM = new OtpServiceClient(tenantUuid, umAuthToken)
    }
}
