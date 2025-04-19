package com.dburyak.example.jwt.lib.msg.redis;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "msg.redis")
@Validated
@Value
@NonFinal
public class RedisMessagingProperties {
    boolean enabled;
    String url;

    @ConstructorBinding
    public RedisMessagingProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("redis://localhost:6379") @NotBlank String url) {
        this.enabled = enabled;
        this.url = url;
    }
}
