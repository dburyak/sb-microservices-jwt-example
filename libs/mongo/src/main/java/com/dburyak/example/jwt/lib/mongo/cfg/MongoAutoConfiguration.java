package com.dburyak.example.jwt.lib.mongo.cfg;

import com.dburyak.example.jwt.lib.mongo.MongoAuditor;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import io.mongock.runner.springboot.EnableMongock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.UUID;

@AutoConfiguration
@EnableMongoAuditing
@EnableMongock
@PropertySource("classpath:/mongodb-defaults.properties")
@PropertySource("classpath:/mongock-defaults.properties")
public class MongoAutoConfiguration {

    @Bean
    public AuditorAware<UUID> mongoAuditor(RequestUtil requestUtil,
            @Value("${spring.application.name}") String serviceName) {
        return new MongoAuditor(requestUtil, serviceName);
    }
}
