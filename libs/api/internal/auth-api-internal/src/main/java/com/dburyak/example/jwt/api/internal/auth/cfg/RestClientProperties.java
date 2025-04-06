package com.dburyak.example.jwt.api.internal.auth.cfg;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.auth")
@Value
public class RestClientProperties {
    String url;
}
