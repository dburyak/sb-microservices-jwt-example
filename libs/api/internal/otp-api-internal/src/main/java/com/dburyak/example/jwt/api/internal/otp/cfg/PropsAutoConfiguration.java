package com.dburyak.example.jwt.api.internal.otp.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties({OTPMsgProperties.class, OTPServiceClientProperties.class})
public class PropsAutoConfiguration {
}
