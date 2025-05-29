package com.dburyak.example.jwt.otp.cfg;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AllOTPProperties.class)
public class PropsCfg {
}
