package com.dburyak.example.jwt.auth.service.converter;

import com.dburyak.example.jwt.api.internal.auth.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserConverter {

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
}
