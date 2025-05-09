package com.dburyak.example.jwt.api.internal.auth.cfg;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "service-client.auth")
@Validated
@Value
@NonFinal
public class AuthServiceClientProperties {
    boolean enabled;
    String url;

    @ConstructorBinding
    public AuthServiceClientProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("http://localhost:8080") @NotBlank String url) {
        this.enabled = enabled;
        this.url = url;
    }
}
