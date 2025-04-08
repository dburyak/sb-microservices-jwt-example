package com.dburyak.example.jwt.test

import org.springframework.web.client.RestClient

import static com.dburyak.example.jwt.lib.req.Headers.BEARER
import static com.dburyak.example.jwt.test.JwtExampleSpec.TENANT_ID_HEADER
import static org.springframework.http.HttpHeaders.AUTHORIZATION

abstract class ServiceClient {
    protected final RestClient rest

    ServiceClient(String baseUrl, String tenantId = null, String jwtToken = null) {
        rest = RestClient.builder()
                .baseUrl(baseUrl)
                .tap {
                    if (jwtToken) {
                        defaultHeader(AUTHORIZATION, BEARER.header + jwtToken)
                    }
                    if (tenantId) {
                        defaultHeader(TENANT_ID_HEADER, tenantId)
                    }
                }
                .build()
    }
}
