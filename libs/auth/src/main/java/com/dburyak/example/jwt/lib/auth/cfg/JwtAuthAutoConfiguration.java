package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.JwtAuthProvider;
import com.dburyak.example.jwt.lib.auth.JwtAuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.JwtFilter;
import com.dburyak.example.jwt.lib.auth.JwtParser;
import com.dburyak.example.jwt.lib.auth.RequestUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

@AutoConfiguration(after = JwtPropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "auth.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtAuthAutoConfiguration {

    @Bean
    public JwtFilter jwtFilter(JwtParser jwtParser, AuthenticationManager authManager, RequestUtil requestUtil) {
        return new JwtFilter(jwtParser, authManager, requestUtil);
    }

    @Bean
    public JwtParser jwtParser(JwtAuthProperties props) {
        return new JwtParser(props);
    }

    @Bean
    public AuthenticationProvider jwtAuthProvider(JwtAuthoritiesMapper rolesMapper) {
        // if jwt-auth is enabled for a service, it must define roles mapper
        return new JwtAuthProvider(rolesMapper);
    }
}
