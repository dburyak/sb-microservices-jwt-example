package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.jwt.JwtAuthProvider;
import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.jwt.JwtFilter;
import com.dburyak.example.jwt.lib.auth.jwt.JwtParser;
import com.dburyak.example.jwt.lib.auth.jwt.NoAuthoritiesMapper;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@AutoConfiguration(after = JwtPropsAutoConfiguration.class)
@EnableWebSecurity
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
    public JwtAuthProvider jwtAuthProvider(AuthoritiesMapper rolesMapper) {
        // if jwt-auth is enabled for a service, it must define roles mapper
        return new JwtAuthProvider(rolesMapper);
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authManager(JwtAuthProvider jwtAuthProvider) {
        return new ProviderManager(jwtAuthProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthoritiesMapper jwtAuthoritiesMapper() {
        return new NoAuthoritiesMapper();
    }
}
