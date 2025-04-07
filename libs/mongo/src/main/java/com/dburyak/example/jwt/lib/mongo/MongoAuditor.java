package com.dburyak.example.jwt.lib.mongo;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;
import java.util.UUID;

import static com.dburyak.example.jwt.lib.req.ServiceUuids.SERVICE_UUIDS;

public class MongoAuditor implements AuditorAware<UUID> {
    private final RequestUtil requestUtil;
    private final UUID serviceUuid;

    public MongoAuditor(RequestUtil requestUtil, String serviceName) {
        this.requestUtil = requestUtil;
        var serviceUuid = SERVICE_UUIDS.get(serviceName);
        if (serviceUuid == null) {
            throw new IllegalStateException("Service UUID is not defined for service: " + serviceName);
        }
        this.serviceUuid = serviceUuid;
    }

    @Override
    public Optional<UUID> getCurrentAuditor() {
        var userUuid = requestUtil.getUserUuid();
        return userUuid != null ? Optional.of(userUuid) : Optional.of(serviceUuid);
    }
}
