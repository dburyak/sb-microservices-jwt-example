package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.lib.req.Attributes
import com.dburyak.example.jwt.lib.req.Headers
import spock.lang.Shared
import spock.lang.Specification

abstract class JwtExampleSpec extends Specification {
    static String AUTH_SERVICE_URL = System.getenv('AUTH_SERVICE_URL') ?: 'http://localhost:8080'
    static String USER_SERVICE_URL = System.getenv('USER_SERVICE_URL') ?: 'http://localhost:8081'
    static String TENANT_SERVICE_URL = System.getenv('TENANT_SERVICE_URL') ?: 'http://localhost:8082'
    static String TENANT_ID_HEADER = Headers.TENANT_UUID.header
    static String TENANT_ID_QUERY_PARAM = Attributes.TENANT_UUID
    static String TENANT_ID = System.getenv('TENANT_ID') ?: 'test-tenant'

    @Shared
    AuthServiceClient authClient = new AuthServiceClient()

    @Shared
    UserServiceClient userClient = new UserServiceClient()

    @Shared
    TenantServiceClient tenantClient = new TenantServiceClient()
}
