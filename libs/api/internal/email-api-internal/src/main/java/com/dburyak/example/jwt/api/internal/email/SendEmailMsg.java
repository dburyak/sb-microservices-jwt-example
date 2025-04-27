package com.dburyak.example.jwt.api.internal.email;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class SendEmailMsg {
    UUID tenantUuid;
    String template;
    String locale;
    String to;
    Map<String, String> params;
}
