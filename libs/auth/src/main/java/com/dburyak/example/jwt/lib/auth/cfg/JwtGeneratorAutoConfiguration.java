package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.jwt.JwtGenerator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = PropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "auth.jwt.generator", name = "enabled", havingValue = "true", matchIfMissing = false)
public class JwtGeneratorAutoConfiguration {

    @Bean
    public JwtGenerator jwtGenerator(JwtAuthProperties props) {
        return new JwtGenerator(props);
    }
}
