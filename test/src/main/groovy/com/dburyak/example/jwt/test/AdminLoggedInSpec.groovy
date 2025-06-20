package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.auth.JwtLoginRequest
import com.dburyak.example.jwt.api.auth.PasswordResetRequest
import com.dburyak.example.jwt.api.common.ExternalId
import spock.lang.Shared

import static com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.Type.PASSWORD_RESET

/**
 * Base spec that creates/deletes tenant for testing, and logs in as the tenant admin user.
 */
abstract class AdminLoggedInSpec extends TenantAutoCreatingSpec {

    @Shared
    String adminAuthToken

    @Shared
    UUID adminUserUuid

    @Shared
    String adminPassword = rndString.nextGraph(25)

    @Shared
    AuthServiceClient authServiceClientAdmin

    @Shared
    UserServiceClient userServiceClientAdmin

    @Shared
    TenantServiceClient tenantServiceClientAdmin

    @Shared
    OtpServiceClient otpServiceClientAdmin

    def setupSpec() {
        def authServiceClient = new AuthServiceClient(tenantUuid)
        authServiceClient.createPasswordResetOTP(adminEmail, ADMIN_DEVICE_ID)
        sleep 100L // wait for OTP to be created asynchronously
        def otpServiceClient = new OtpServiceClient(tenantUuid)
        def otpResp = otpServiceClient.findExternallyIdentifiedOTP(adminEmail, ADMIN_DEVICE_ID, PASSWORD_RESET)
        authServiceClient.resetPassword(PasswordResetRequest.builder()
                .deviceId(ADMIN_DEVICE_ID)
                .externalId(new ExternalId(adminEmail))
                .otp(otpResp.code)
                .newPassword(adminPassword)
                .build(), tenantUuid)
        def loginAdminResp = authServiceClient.jwtLogin(JwtLoginRequest.builder()
                .username(ADMIN_USERNAME)
                .password(adminPassword)
                .deviceId(ADMIN_DEVICE_ID)
                .build())
        assert loginAdminResp.accessToken
        adminAuthToken = loginAdminResp.accessToken
        adminUserUuid = loginAdminResp.userUuid
        authServiceClientAdmin = new AuthServiceClient(tenantUuid, adminAuthToken)
        userServiceClientAdmin = new UserServiceClient(tenantUuid, adminAuthToken)
        tenantServiceClientAdmin = new TenantServiceClient(tenantUuid, adminAuthToken)
        otpServiceClientAdmin = new OtpServiceClient(tenantUuid, adminAuthToken)
    }
}
