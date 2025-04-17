package com.dburyak.example.jwt.api.internal.tenant.cfg;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "service-client.tenant")
@Validated
@Value
@NonFinal
public class TenantServiceClientProperties {
    boolean enabled;
    String url;

    @ConstructorBinding
    public TenantServiceClientProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("http://localhost:8082") @NotBlank String url) {
        this.enabled = enabled;
        this.url = url;
    }
}
