package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.JwtFilter;
import com.dburyak.example.jwt.lib.auth.JwtParser;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

@AutoConfiguration(after = JwtPropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "auth.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtAuthAutoConfiguration {

    @Bean
    public JwtFilter jwtFilter(JwtParser jwtParser, AuthenticationManager authManager) {
        return new JwtFilter(jwtParser, authManager);
    }

    @Bean
    public JwtParser jwtParser(JwtAuthProperties props) {
        return new JwtParser(props);
    }
}
