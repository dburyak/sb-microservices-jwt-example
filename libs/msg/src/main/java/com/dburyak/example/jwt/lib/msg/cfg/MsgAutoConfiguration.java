package com.dburyak.example.jwt.lib.msg.cfg;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.dburyak.example.jwt.lib.msg.cfg.MsgProperties.ExecType.VIRTUAL;

@AutoConfiguration(after = PropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "msg", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MsgAutoConfiguration {

    @Bean
    @Messaging
    public Executor msgSubscriberExecutor(MsgProperties props) {
        if (props.getExecType() == VIRTUAL) {
            return Executors.newVirtualThreadPerTaskExecutor();
        }
        return new ThreadPoolExecutor(props.getMinThreads(), props.getMaxThreads(), 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
    }
}
