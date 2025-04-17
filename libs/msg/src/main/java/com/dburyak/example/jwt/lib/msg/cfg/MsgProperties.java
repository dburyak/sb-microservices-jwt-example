package com.dburyak.example.jwt.lib.msg.cfg;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "msg")
@Validated
@Value
@NonFinal
public class MsgProperties {
    boolean enabled;

    @ConstructorBinding
    public MsgProperties(@DefaultValue("false") boolean enabled) {
        this.enabled = enabled;
    }
}
