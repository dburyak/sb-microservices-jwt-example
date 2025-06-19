package com.dburyak.example.jwt.user.service.converter;

import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class UserConverter {

    public com.dburyak.example.jwt.user.domain.User toDomain(User user, UUID tenantUuid) {
        return com.dburyak.example.jwt.user.domain.User.builder()
                .tenantUuid(tenantUuid)
                .uuid(user.getUuid())
                .externalId(toDomain(user.getExternalId()))
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .contactInfo(toDomain(user.getContactInfo()))
                .profileIcon(user.getProfileIcon())
                .build();
    }

    public User toApiModel(com.dburyak.example.jwt.user.domain.User user) {
        return User.builder()
                .uuid(user.getUuid())
                .externalId(toApiModel(user.getExternalId()))
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .contactInfo(toApiModel(user.getContactInfo()))
                .profileIcon(user.getProfileIcon())
                .createdDate(user.getCreatedDate())
                .createdBy(user.getCreatedBy())
                .lastModifiedDate(user.getLastModifiedDate())
                .lastModifiedBy(user.getLastModifiedBy())
                .build();
    }

    public com.dburyak.example.jwt.user.domain.ContactInfo toDomain(ContactInfo contactInfo) {
        return com.dburyak.example.jwt.user.domain.ContactInfo.builder()
                .email(contactInfo.getEmail())
                .build();
    }

    public ContactInfo toApiModel(com.dburyak.example.jwt.user.domain.ContactInfo contactInfo) {
        return ContactInfo.builder()
                .email(contactInfo.getEmail())
                .build();
    }

    public com.dburyak.example.jwt.lib.mongo.ExternalId toDomain(ExternalId externalId) {
        return com.dburyak.example.jwt.lib.mongo.ExternalId.builder()
                .email(toDomainEmail(externalId.getEmail()))
                .build();
    }

    public ExternalId toApiModel(com.dburyak.example.jwt.lib.mongo.ExternalId externalId) {
        return ExternalId.builder()
                .email(externalId.getEmail())
                .build();
    }

    public String toDomainEmail(String email) {
        return email != null ? email.toLowerCase() : null;
    }

    public com.dburyak.example.jwt.api.auth.User toApiModelAuth(User reqUser,
            com.dburyak.example.jwt.user.domain.User domainUser, Set<String> roles) {
        return com.dburyak.example.jwt.api.auth.User.builder()
                .uuid(domainUser.getUuid())
                .username(reqUser.getUsername())
                .password(reqUser.getPassword())
                .roles(roles)
                .build();
    }
}
