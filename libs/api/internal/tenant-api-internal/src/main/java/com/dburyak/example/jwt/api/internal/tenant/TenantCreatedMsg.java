package com.dburyak.example.jwt.api.internal.tenant;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class TenantCreatedMsg {
    UUID uuid;
    String adminEmail;
}
