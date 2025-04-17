package com.dburyak.example.jwt.lib.msg.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(MsgProperties.class)
public class PropsAutoConfiguration {
}
