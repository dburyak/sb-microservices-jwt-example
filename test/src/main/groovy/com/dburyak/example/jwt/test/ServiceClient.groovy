package com.dburyak.example.jwt.test

import org.springframework.web.client.RestClient

import static com.dburyak.example.jwt.lib.req.Headers.BEARER
import static com.dburyak.example.jwt.lib.req.Headers.TENANT_UUID
import static org.springframework.http.HttpHeaders.AUTHORIZATION

abstract class ServiceClient<T extends ServiceClient<T>> {
    private final String baseUrl
    private final UUID tenantUuid
    private final String jwtToken

    protected final RestClient rest

    ServiceClient(String baseUrl, UUID tenantUuid = null, String jwtToken = null) {
        this.baseUrl = baseUrl
        this.tenantUuid = tenantUuid
        this.jwtToken = jwtToken
        rest = RestClient.builder()
                .baseUrl(baseUrl)
                .tap {
                    if (jwtToken) {
                        defaultHeader(AUTHORIZATION, BEARER + jwtToken)
                    }
                    if (tenantUuid) {
                        defaultHeader(TENANT_UUID.header, tenantUuid.toString())
                    }
                }
                .build()
    }

    protected abstract T createWith(UUID tenantUuid = null, String jwtToken = null)

    T with(Map<String, ?> args) {
        createWith(args.tenantUuid ?: tenantUuid, args.jwtToken ?: jwtToken)
    }

}
