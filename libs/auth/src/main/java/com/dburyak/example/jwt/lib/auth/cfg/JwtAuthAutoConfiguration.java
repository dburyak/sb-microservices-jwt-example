package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.FilterRegistrationHttpConfigurer;
import com.dburyak.example.jwt.lib.auth.SecurityFilterChainHttpConfigurer;
import com.dburyak.example.jwt.lib.auth.jwt.JwtAuthExtractor;
import com.dburyak.example.jwt.lib.auth.jwt.JwtAuthProvider;
import com.dburyak.example.jwt.lib.auth.jwt.JwtFilter;
import com.dburyak.example.jwt.lib.auth.jwt.JwtParser;
import com.dburyak.example.jwt.lib.auth.jwt.NoAuthoritiesMapper;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration(after = PropsAutoConfiguration.class)
@EnableWebSecurity
@ConditionalOnProperty(prefix = "auth.jwt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JwtAuthAutoConfiguration {

    @Bean
    public JwtParser jwtParser(JwtAuthProperties props) {
        return new JwtParser(props);
    }

    @Bean
    public JwtAuthExtractor jwtAuthExtractor(JwtParser jwtParser) {
        return new JwtAuthExtractor(jwtParser);
    }

    @Bean
    public JwtFilter jwtFilter(JwtAuthExtractor jwtAuthExtractor, AuthenticationManager authManager,
            RequestUtil requestUtil) {
        return new JwtFilter(jwtAuthExtractor, authManager, requestUtil);
    }

    @Bean
    public JwtAuthProvider jwtAuthProvider(AuthoritiesMapper rolesMapper) {
        // if jwt-auth is enabled for a service, it must define roles mapper
        return new JwtAuthProvider(rolesMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthoritiesMapper jwtAuthoritiesMapper() {
        return new NoAuthoritiesMapper();
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChainHttpConfigurer jwtFilterConfigurer(JwtFilter jwtFilter) {
        return new FilterRegistrationHttpConfigurer(jwtFilter, jwtFilter.getOrder());
    }
}
