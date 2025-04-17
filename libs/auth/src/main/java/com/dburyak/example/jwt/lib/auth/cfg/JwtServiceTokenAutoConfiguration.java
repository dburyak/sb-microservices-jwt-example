package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.jwt.JwtGenerator;
import com.dburyak.example.jwt.lib.auth.jwt.JwtServiceTokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {PropsAutoConfiguration.class, JwtGeneratorAutoConfiguration.class})
@ConditionalOnProperty(prefix = "auth.jwt.service-token", name = "enabled", havingValue = "true",
        matchIfMissing = false)
public class JwtServiceTokenAutoConfiguration {

    @Bean
    public JwtServiceTokenManager serviceTokenManager(JwtAuthProperties props, JwtGenerator jwtGenerator,
            @Value("${spring.application.name}") String serviceName) {
        return new JwtServiceTokenManager(props, jwtGenerator, serviceName);
    }
}
