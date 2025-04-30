package com.dburyak.example.jwt.user.service;

import com.dburyak.example.jwt.api.internal.auth.AuthServiceClientInternal;
import com.dburyak.example.jwt.api.user.ContactInfo;
import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.user.repository.UserRepository;
import com.dburyak.example.jwt.user.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final AuthServiceClientInternal authServiceClientInternal;

    public User create(UUID tenantUuid, User req) {
        var user = userConverter.toDomain(req, tenantUuid);
        user.setUuid(UUID.randomUUID());
        var savedUser = userRepository.save(user);
        publishUserCreatedEvent(tenantUuid, req, savedUser);
        return userConverter.toApiModel(savedUser);
    }

    public void deleteAllByTenantUuid(UUID tenantUuid) {
        userRepository.deleteAllByTenantUuid(tenantUuid);
    }

    private void publishUserCreatedEvent(UUID tenantUuid, User reqUser,
            com.dburyak.example.jwt.user.domain.User domainUser) {
        // in a real-world app we'd rather publish it as an event, and let the relevant services handle it
        authServiceClientInternal.createUser(tenantUuid, userConverter.toApiModelAuth(reqUser, domainUser));
    }

    public User findByUuid(UUID tenantUuid, UUID userUuid) {
        var user = userRepository.findByTenantUuidAndUuid(tenantUuid, userUuid);
        if (user == null) {
            throw new NotFoundException("User(uuid=%s)".formatted(userUuid));
        }
        return userConverter.toApiModel(user);
    }

    public ContactInfo findContactInfoByUuid(UUID tenantUuid, UUID userUuid) {
        var contactInfo = userRepository.findContactInfoByTenantUuidAndUuid(tenantUuid, userUuid);
        if (contactInfo == null) {
            throw new NotFoundException("User.ContactInfo(uuid=%s)".formatted(userUuid));
        }
        return userConverter.toApiModel(contactInfo);
    }
}
