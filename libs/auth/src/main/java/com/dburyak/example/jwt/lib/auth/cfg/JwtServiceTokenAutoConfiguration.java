package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.JwtGenerator;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = JwtPropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "auth.jwt.generator.service-token", name = "enabled", havingValue = "true", matchIfMissing = false)
public class JwtServiceTokenAutoConfiguration {

    @Bean
    public ServiceTokenManager serviceTokenManager(JwtAuthProperties props, JwtGenerator jwtGenerator) {
        if (StringUtils.isBlank(props.getGenerator().getSubject())) {
            throw new IllegalArgumentException("Subject must be defined for JWT generator");
        }
        return new ServiceTokenManager(props, jwtGenerator);
    }
}
