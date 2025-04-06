package com.dburyak.example.jwt.api.internal.auth.cfg;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "service-client.auth")
@Value
public class AuthServiceClientProperties {
    boolean enabled;
    String url;
}
