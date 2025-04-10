package com.dburyak.example.jwt.tenant.service;

import com.dburyak.example.jwt.api.tenant.Tenant;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.tenant.repository.TenantRepository;
import com.dburyak.example.jwt.tenant.service.converter.TenantConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantService {
    private static final String NOT_FOUND_MSG = "Tenant(uuid=%s)";
    private final TenantRepository tenantRepository;
    private final TenantConverter converter;

    public Tenant create(Tenant req) {
        var tenant = converter.toDomain(req);
        tenant.setUuid(UUID.randomUUID());
        var savedTenant = tenantRepository.save(tenant);
        return converter.toApiModel(savedTenant);
    }

    public Tenant get(UUID tenantUuid) {
        var tenant = tenantRepository.findByUuid(tenantUuid);
        if (tenant == null) {
            throw new NotFoundException(NOT_FOUND_MSG.formatted(tenantUuid));
        }
        return converter.toApiModel(tenant);
    }

    public boolean delete(UUID tenantUuid) {
        var deleted = tenantRepository.deleteByUuid(tenantUuid);
        if (!deleted) {
            throw new NotFoundException(NOT_FOUND_MSG.formatted(tenantUuid));
        }
        return true;
    }

    public void verifyExists(UUID tenantUuid) {
        var exists = tenantRepository.existsByUuid(tenantUuid);
        if (!exists) {
            throw new NotFoundException(NOT_FOUND_MSG.formatted(tenantUuid));
        }
    }
}
