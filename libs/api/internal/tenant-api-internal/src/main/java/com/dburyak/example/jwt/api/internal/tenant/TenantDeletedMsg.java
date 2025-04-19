package com.dburyak.example.jwt.api.internal.tenant;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TenantDeletedMsg {
    UUID uuid;
}
