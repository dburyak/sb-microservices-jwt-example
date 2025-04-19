package com.dburyak.example.jwt.api.internal.tenant;

import lombok.Builder;
import lombok.Value;

import java.util.Set;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class UserCreatedMsg {
    UUID tenantUuid;
    UUID userUuid;
    String username;
    String password;
    Set<String> roles;
}
