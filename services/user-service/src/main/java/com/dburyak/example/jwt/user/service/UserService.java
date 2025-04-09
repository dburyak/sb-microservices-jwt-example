package com.dburyak.example.jwt.user.service;

import com.dburyak.example.jwt.api.internal.auth.AuthServiceClient;
import com.dburyak.example.jwt.api.user.User;
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
    private final AuthServiceClient authServiceClient;

    public User create(String tenantId, User req) {
        var user = userConverter.toDomain(req, tenantId);
        user.setUuid(UUID.randomUUID());
        var savedUser = userRepository.save(user);
        publishUserCreatedEvent(tenantId, req, savedUser);
        return userConverter.toApiModel(savedUser);
    }

    private void publishUserCreatedEvent(String tenantId, User reqUser,
            com.dburyak.example.jwt.user.domain.User domainUser) {
        // in a real-world app we'd rather publish it as an event, and let the relevant services handle it
        authServiceClient.createUser(tenantId, userConverter.toApiModelAuth(reqUser, domainUser));
    }
}
