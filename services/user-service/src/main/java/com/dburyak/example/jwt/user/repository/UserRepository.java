package com.dburyak.example.jwt.user.repository;

import com.dburyak.example.jwt.user.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
