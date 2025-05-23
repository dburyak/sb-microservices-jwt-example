package com.dburyak.example.jwt.test


import com.dburyak.example.jwt.api.tenant.Tenant

import static BaseSpec.TENANT_SERVICE_URL
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.MediaType.APPLICATION_JSON

class TenantServiceClient extends ServiceClient {

    TenantServiceClient(UUID tenantUuid = null, String jwtToken = null) {
        super(TENANT_SERVICE_URL, tenantUuid, jwtToken)
    }

    @Override
    protected TenantServiceClient createWith(UUID tenantUuid = null, String jwtToken = null) {
        new TenantServiceClient(tenantUuid, jwtToken)
    }

    boolean tenantExists(UUID tenantUuid, String tenantName) {
        assert tenantUuid || tenantName
        def resp = rest.get()
                .uri {
                    it.path(TENANTS + TENANT_EXISTS)
                            .tap {
                                if (tenantUuid) {
                                    queryParam(QueryParams.TENANT_UUID, tenantUuid)
                                }
                                if (tenantName) {
                                    queryParam(QueryParams.TENANT_NAME, tenantName)
                                }
                            }
                            .build()
                }
                .retrieve()
                .toBodilessEntity()
        resp.statusCode == OK
    }

    boolean tenantExistsByName(String tenantName) {
        tenantExists(null, tenantName)
    }

    boolean tenantExistsByUuid(UUID uuid) {
        tenantExists(uuid, null)
    }

    Tenant create(Tenant tenant) {
        rest.post()
                .uri { it.path(TENANTS).build() }
                .contentType(APPLICATION_JSON)
                .body(tenant)
                .retrieve()
                .body(Tenant)
    }

    Tenant get(String tenantId) {
        rest.get()
                .uri {
                    it.path(TENANTS)
                            .queryParam(TENANT_ID_QUERY_PARAM, tenantId)
                            .build()
                }
                .retrieve()
                .body(Tenant)
    }

    void delete(String tenantId) {
        rest.delete()
                .uri {
                    it.path(TENANTS)
                            .queryParam(TENANT_ID_QUERY_PARAM, tenantId)
                            .build()
                }
                .retrieve()
                .toBodilessEntity()
    }
}
