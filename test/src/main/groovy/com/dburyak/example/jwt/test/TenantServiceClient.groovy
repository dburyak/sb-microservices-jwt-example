package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.tenant.Tenant

import static BaseSpec.TENANT_SERVICE_URL
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_NAME
import static com.dburyak.example.jwt.api.tenant.Paths.PATH_TENANT_BY_UUID
import static com.dburyak.example.jwt.api.tenant.Paths.TENANTS
import static org.springframework.http.MediaType.APPLICATION_JSON

class TenantServiceClient extends ServiceClient {

    TenantServiceClient(UUID tenantUuid = null, String jwtToken = null) {
        super(TENANT_SERVICE_URL, tenantUuid, jwtToken)
    }

    @Override
    protected TenantServiceClient createWith(UUID tenantUuid = null, String jwtToken = null) {
        new TenantServiceClient(tenantUuid, jwtToken)
    }

    Tenant create(Tenant tenant) {
        rest.post()
                .uri { it.path(TENANTS).build() }
                .contentType(APPLICATION_JSON)
                .body(tenant)
                .retrieve()
                .body(Tenant)
    }

    Tenant getByUuid(String tenantUuid) {
        rest.get()
                .uri { it.path(PATH_TENANT_BY_UUID).build(tenantUuid) }
                .retrieve()
                .body(Tenant)
    }

    Tenant getByName(String tenantName) {
        rest.get()
                .uri { it.path(PATH_TENANT_BY_NAME).build(tenantName) }
                .retrieve()
                .body(Tenant)
    }

    void deleteByUuid(String tenantUuid) {
        rest.delete()
                .uri { it.path(PATH_TENANT_BY_UUID).build(tenantUuid) }
                .retrieve()
                .toBodilessEntity()
    }

    void deleteByName(String tenantName) {
        rest.delete()
                .uri { it.path(PATH_TENANT_BY_NAME).build(tenantName) }
                .retrieve()
                .toBodilessEntity()
    }
}
