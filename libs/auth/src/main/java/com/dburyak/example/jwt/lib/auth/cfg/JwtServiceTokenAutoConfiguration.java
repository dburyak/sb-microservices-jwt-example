package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.JwtGenerator;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {JwtPropsAutoConfiguration.class, JwtGeneratorAutoConfiguration.class})
@ConditionalOnProperty(prefix = "auth.jwt.service-token", name = "enabled", havingValue = "true",
        matchIfMissing = false)
public class JwtServiceTokenAutoConfiguration {

    @Bean
    public ServiceTokenManager serviceTokenManager(JwtAuthProperties props, JwtGenerator jwtGenerator,
            @Value("${spring.application.name") String serviceName) {
        return new ServiceTokenManager(props, jwtGenerator, serviceName);
    }
}
