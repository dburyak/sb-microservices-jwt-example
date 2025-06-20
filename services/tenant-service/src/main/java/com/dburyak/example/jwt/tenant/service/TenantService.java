package com.dburyak.example.jwt.tenant.service;

import com.dburyak.example.jwt.api.internal.tenant.TenantCreatedMsg;
import com.dburyak.example.jwt.api.internal.tenant.TenantDeletedMsg;
import com.dburyak.example.jwt.api.internal.tenant.cfg.TenantMsgProperties;
import com.dburyak.example.jwt.api.tenant.Tenant;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue;
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
    private final TenantMsgProperties msgProps;
    private final PointToPointMsgQueue msgQueue;

    public Tenant create(Tenant req) {
        var tenant = converter.toDomain(req);
        var newTenantUuid = UUID.randomUUID();
        tenant.setUuid(newTenantUuid);
        tenant.setTenantUuid(newTenantUuid);
        var savedTenant = tenantRepository.save(tenant);
        var tenantCreatedMsg = TenantCreatedMsg.builder()
                .uuid(savedTenant.getUuid())
                .adminEmail(req.getAdminEmail())
                .build();
        msgQueue.publish(msgProps.getTopics().getTenantCreated().getTopicName(), tenantCreatedMsg);
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

    public boolean deleteByUuid(UUID tenantUuid) {
        var numDeleted = tenantRepository.deleteByUuid(tenantUuid);
        if (numDeleted == 0) {
            throw new NotFoundException(NOT_FOUND_BY_UUID_MSG.formatted(tenantUuid));
        }
        msgQueue.publish(msgProps.getTopics().getTenantDeleted().getTopicName(), new TenantDeletedMsg(tenantUuid));
        return true;
    }

    public boolean deleteByName(String tenantName) {
        var deletedTenantUuid = tenantRepository.deleteByName(tenantName);
        if (deletedTenantUuid == null) {
            throw new NotFoundException(NOT_FOUND_BY_NAME_MSG.formatted(tenantName));
        }
        msgQueue.publish(msgProps.getTopics().getTenantDeleted().getTopicName(),
                new TenantDeletedMsg(deletedTenantUuid));
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
}
