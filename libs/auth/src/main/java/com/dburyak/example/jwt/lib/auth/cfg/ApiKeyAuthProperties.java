package com.dburyak.example.jwt.lib.auth.cfg;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "auth.api-key")
@Validated
@Value
@NonFinal
public class ApiKeyAuthProperties {

    /**
     * Whether to enable authentication using api keys.
     */
    boolean enabled;
}
