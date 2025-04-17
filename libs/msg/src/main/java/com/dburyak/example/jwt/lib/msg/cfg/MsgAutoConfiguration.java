package com.dburyak.example.jwt.lib.msg.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@AutoConfiguration(after = PropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "msg", name = "enabled", havingValue = "true")
public class MsgAutoConfiguration {

    @Bean
    @Messaging
    public Executor msgSubscriberExecutor(MsgProperties props) {
        return Executors.new;
    }
}
