package com.dburyak.example.jwt.api.internal.email;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NoArgsConstructor(force = true, access = PRIVATE)
public class SendEmailMsg {
    String template;
    String locale;
    String to;
    Map<String, String> params;
}
