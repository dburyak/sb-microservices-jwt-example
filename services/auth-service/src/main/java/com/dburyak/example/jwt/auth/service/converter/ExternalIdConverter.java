package com.dburyak.example.jwt.auth.service.converter;

import com.dburyak.example.jwt.api.common.ExternalId;
import org.springframework.stereotype.Component;

@Component
public class ExternalIdConverter {

    public com.dburyak.example.jwt.lib.mongo.ExternalId toDomain(ExternalId externalId) {
        return new com.dburyak.example.jwt.lib.mongo.ExternalId(toDomainEmail(externalId.getEmail()));
    }

    public ExternalId toApiModel(com.dburyak.example.jwt.lib.mongo.ExternalId externalId) {
        return new ExternalId(externalId.getEmail());
    }

    public String toDomainEmail(String email) {
        return email != null ? email.strip().toLowerCase() : null;
    }
}
