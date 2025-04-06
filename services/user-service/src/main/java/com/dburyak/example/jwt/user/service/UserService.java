package com.dburyak.example.jwt.user.service;

import com.dburyak.example.jwt.api.user.User;
import com.dburyak.example.jwt.user.repository.UserRepository;
import com.dburyak.example.jwt.user.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final RestClient rest;

    public User create(String tenantId, User req) {
        var user = userConverter.toDomain(req, tenantId);
        var savedUser = userRepository.save(user);
        var authUserReq = userConverter.toApiModelAuth(req);
        rest.post()
                .uri()
        return userConverter.toApiModel(savedUser);
    }
}
