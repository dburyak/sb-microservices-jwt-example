package com.dburyak.example.jwt.tenant.service;

import com.dburyak.example.jwt.api.tenant.Tenant;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.tenant.repository.TenantRepository;
import com.dburyak.example.jwt.tenant.service.converter.TenantConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
@RequiredArgsConstructor
public class TenantService {
    private static final String NOT_FOUND_BY_UUID_MSG = "Tenant(uuid=%s)";
    private static final String NOT_FOUND_BY_NAME_MSG = "Tenant(name=%s)";
    private final TenantRepository tenantRepository;
    private final TenantConverter converter;

    public Tenant create(Tenant req) {
        var tenant = converter.toDomain(req);
        tenant.setUuid(UUID.randomUUID());
        var savedTenant = tenantRepository.save(tenant);
        publishTenantCreatedEvent(savedTenant, req);
        return converter.toApiModel(savedTenant);
    }

    public Tenant get(UUID tenantUuid) {
        var tenant = tenantRepository.findByUuid(tenantUuid);
        if (tenant == null) {
            throw new NotFoundException(NOT_FOUND_BY_UUID_MSG.formatted(tenantUuid));
        }
        return converter.toApiModel(tenant);
    }

    public Tenant get(String tenantName) {
        if (isBlank(tenantName)) {
            throw new IllegalArgumentException("Tenant name cannot be blank");
        }
        var tenant = tenantRepository.findByName(tenantName);
        if (tenant == null) {
            throw new NotFoundException(NOT_FOUND_BY_NAME_MSG.formatted(tenantName));
        }
        return converter.toApiModel(tenant);
    }

    public boolean delete(UUID tenantUuid) {
        var deleted = tenantRepository.deleteByUuid(tenantUuid);
        if (!deleted) {
            throw new NotFoundException(NOT_FOUND_BY_UUID_MSG.formatted(tenantUuid));
        }
        publishTenantDeletedEvent(tenantUuid);
        return true;
    }

    public void verifyExistsByUuid(UUID tenantUuid) {
        if (tenantUuid == null) {
            throw new IllegalArgumentException("Tenant UUID cannot be null");
        }
        var exists = tenantRepository.existsByUuid(tenantUuid);
        if (!exists) {
            throw new NotFoundException(NOT_FOUND_BY_UUID_MSG.formatted(tenantUuid));
        }
    }

    public void verifyExistsByName(String tenantName) {
        if (isBlank(tenantName)) {
            throw new IllegalArgumentException("Tenant name cannot be blank");
        }
        var exists = tenantRepository.existsByName(tenantName);
        if (!exists) {
            throw new NotFoundException(NOT_FOUND_BY_NAME_MSG.formatted(tenantName));
        }
    }

    private void publishTenantCreatedEvent(com.dburyak.example.jwt.tenant.domain.Tenant savedTenant, Tenant req) {
        // TODO: implement
    }

    private void publishTenantDeletedEvent(UUID tenantUuid) {
        // TODO: implement
    }
}
