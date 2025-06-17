package com.dburyak.example.jwt.api.internal.tenant;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class TenantDeletedMsg {
    UUID uuid;
}
