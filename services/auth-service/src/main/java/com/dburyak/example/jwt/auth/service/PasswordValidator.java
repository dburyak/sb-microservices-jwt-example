package com.dburyak.example.jwt.auth.service;

import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {
    public boolean isValid(String password) {
        // here we can implement password rules (e.g., length, complexity, etc.)
        return true;
    }
}
