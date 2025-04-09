package com.dburyak.example.jwt.user.service.converter;

import com.dburyak.example.jwt.api.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public com.dburyak.example.jwt.user.domain.User toDomain(User user, String tenantId) {
        return com.dburyak.example.jwt.user.domain.User.builder()
                .tenantUuid(tenantId)
                .uuid(user.getUuid())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .profileIcon(user.getProfileIcon())
                .build();
    }

    public User toApiModel(com.dburyak.example.jwt.user.domain.User user) {
        return User.builder()
                .uuid(user.getUuid())
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .profileIcon(user.getProfileIcon())
                .createdDate(user.getCreatedDate())
                .createdBy(user.getCreatedBy())
                .lastModifiedDate(user.getLastModifiedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .build();
    }

    public com.dburyak.example.jwt.api.internal.auth.User toApiModelAuth(User reqUser,
            com.dburyak.example.jwt.user.domain.User domainUser) {
        return com.dburyak.example.jwt.api.internal.auth.User.builder()
                .uuid(domainUser.getUuid())
                .username(reqUser.getUsername())
                .password(reqUser.getPassword())
                .build();
    }
}
