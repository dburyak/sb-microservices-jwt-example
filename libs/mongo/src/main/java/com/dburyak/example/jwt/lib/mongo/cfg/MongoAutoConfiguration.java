package com.dburyak.example.jwt.lib.mongo.cfg;

import com.dburyak.example.jwt.lib.mongo.MongoAuditor;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.UUID;

@AutoConfiguration
@EnableMongoAuditing
public class MongoAutoConfiguration {

    @Bean
    public AuditorAware<UUID> mongoAuditor(RequestUtil requestUtil,
            @Value("${spring.application.name}") String serviceName) {
        return new MongoAuditor(requestUtil, serviceName);
    }
}
