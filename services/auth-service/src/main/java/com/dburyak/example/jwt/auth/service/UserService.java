package com.dburyak.example.jwt.auth.service;

import com.dburyak.example.jwt.api.internal.auth.User;
import com.dburyak.example.jwt.auth.repository.UserRepository;
import com.dburyak.example.jwt.auth.service.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserConverter converter;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User create(User req) {
        var user = converter.toDomain(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        var savedUser = repository.save(user);
        return converter.toApiModel(savedUser);
    }
}
