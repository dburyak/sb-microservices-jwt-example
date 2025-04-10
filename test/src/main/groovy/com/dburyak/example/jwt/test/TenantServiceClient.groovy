package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.tenant.Tenant

import static com.dburyak.example.jwt.api.tenant.Paths.TENANT_EXISTS
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS_ROOT
import static com.dburyak.example.jwt.test.JwtExampleSpec.TENANT_SERVICE_URL
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON

class TenantServiceClient extends ServiceClient {

    TenantServiceClient(String tenantId = null, String jwtToken = null) {
        super(TENANT_SERVICE_URL, tenantId, jwtToken)
    }

    boolean tenantExists(String tenantId = null) {
        def resp = rest.get()
                .uri {
                    it.path(TENANTS_ROOT + TENANT_EXISTS)
                            .tap {
                                if (tenantId) {
                                    queryParam(TENANT_ID_QUERY_PARAM, tenantId)
                                }
                            }
                            .build()
                }
                .retrieve()
                .toBodilessEntity()
        resp.statusCode == OK
    }

    Tenant create(Tenant tenant) {
        rest.post()
                .uri { it.path(TENANTS_ROOT).build() }
                .contentType(APPLICATION_JSON)
                .body(tenant)
                .retrieve()
                .body(Tenant)
    }

    Tenant get(String tenantId) {
        rest.get()
                .uri {
                    it.path(TENANTS_ROOT)
                            .queryParam(TENANT_ID_QUERY_PARAM, tenantId)
                            .build()
                }
                .retrieve()
                .body(Tenant)
    }

    void delete(String tenantId) {
        rest.delete()
                .uri {
                    it.path(TENANTS_ROOT)
                            .queryParam(TENANT_ID_QUERY_PARAM, tenantId)
                            .build()
                }
                .retrieve()
                .toBodilessEntity()
    }
}
