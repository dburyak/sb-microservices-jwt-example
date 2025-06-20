package com.dburyak.example.jwt.test


import spock.lang.Shared

import static com.dburyak.example.jwt.lib.req.ReservedIdentifiers.SA_TENANT_UUID

/**
 * Base spec that logs in as SA and has configured service clients for making calls with SA token (permissions).
 */
abstract class SuperAdminLoggedInSpec extends BaseSpec {

    @Shared
    String saAuthToken

    @Shared
    UUID saUserUuid

    @Shared
    AuthServiceClient authServiceClientSA

    @Shared
    UserServiceClient userServiceClientSA

    @Shared
    TenantServiceClient tenantServiceClientSA

    def setupSpec() {
        def authServiceClient = new AuthServiceClient(SA_TENANT_UUID)
        def loginSAResp = authServiceClient.jwtLogin(SA_LOGIN_REQUEST)
        assert loginSAResp.accessToken
        saAuthToken = loginSAResp.accessToken
        saUserUuid = loginSAResp.userUuid
        authServiceClientSA = new AuthServiceClient(SA_TENANT_UUID, saAuthToken)
        userServiceClientSA = new UserServiceClient(SA_TENANT_UUID, saAuthToken)
        tenantServiceClientSA = new TenantServiceClient(SA_TENANT_UUID, saAuthToken)
    }
}
