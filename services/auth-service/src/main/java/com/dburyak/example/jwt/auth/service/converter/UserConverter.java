package com.dburyak.example.jwt.auth.service.converter;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ExternalIdConverter externalIdConverter;

    public User toApiModel(com.dburyak.example.jwt.auth.domain.User user) {
        return User.builder()
                .uuid(user.getUuid())
                .username(user.getUsername())
                .roles(user.getRoles())
                .createdBy(user.getCreatedBy())
                .createdDate(user.getCreatedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .lastModifiedDate(user.getLastModifiedDate())
                .build();
    }

    public com.dburyak.example.jwt.auth.domain.User toDomain(User user, UUID tenantUuid) {
        return com.dburyak.example.jwt.auth.domain.User.builder()
                .tenantUuid(tenantUuid)
                .uuid(user.getUuid())
                .username(user.getUsername())
                .roles(user.getRoles())
                .createdBy(user.getCreatedBy())
                .createdDate(user.getCreatedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .lastModifiedDate(user.getLastModifiedDate())
                .build();
    }

    public ExternalId toApiModel(com.dburyak.example.jwt.lib.mongo.ExternalId externalId) {
        return externalIdConverter.toApiModel(externalId);
    }

    public com.dburyak.example.jwt.lib.mongo.ExternalId toDomain(ExternalId externalId) {
        return externalIdConverter.toDomain(externalId);
    }

    public String toDomainPassword(String password) {
        // any more complex sanitization can be done here if needed, we'll just strip blank spaces for now
        return password.strip();
    }
}
