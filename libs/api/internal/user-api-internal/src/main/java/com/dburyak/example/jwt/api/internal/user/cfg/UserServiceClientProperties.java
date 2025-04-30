package com.dburyak.example.jwt.api.internal.user.cfg;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "service-client.user")
@Validated
@Value
@NonFinal
public class UserServiceClientProperties {
    boolean enabled;
    String url;

    @ConstructorBinding
    public UserServiceClientProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("http://localhost:8081") @NotBlank String url) {
        this.enabled = enabled;
        this.url = url;
    }
}
