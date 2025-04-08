package com.dburyak.example.jwt.auth.service;

import com.dburyak.example.jwt.api.internal.auth.User;
import com.dburyak.example.jwt.auth.repository.UserRepository;
import com.dburyak.example.jwt.auth.service.converter.UserConverter;
import com.dburyak.example.jwt.lib.auth.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter converter;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User create(String tenantId, User req, Set<Role> roles) {
        var user = converter.toDomain(req, tenantId);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRoles(roles.stream().map(Role::getName).collect(toSet()));
        var savedUser = repository.save(user);
        return converter.toApiModel(savedUser);
    }
}
