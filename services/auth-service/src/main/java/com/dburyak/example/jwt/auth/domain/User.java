package com.dburyak.example.jwt.auth.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
    private String id;

    private String username;
    private String password;
}
