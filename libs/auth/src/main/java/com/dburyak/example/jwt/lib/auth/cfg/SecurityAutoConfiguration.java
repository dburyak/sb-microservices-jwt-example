package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import com.dburyak.example.jwt.lib.auth.SecurityFilterChainHttpConfigurer;
import com.dburyak.example.jwt.lib.auth.apikey.ApiKeyAuthProvider;
import com.dburyak.example.jwt.lib.auth.jwt.JwtAuthProvider;
import com.dburyak.example.jwt.lib.req.TenantUuidExtractionFilter;
import com.dburyak.example.jwt.lib.req.TenantVerificationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.Optional;

import static com.dburyak.example.jwt.lib.auth.StandardAuthorities.ACTUATOR_READ;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@AutoConfiguration
@EnableWebSecurity
@Slf4j
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(AuthenticationManager.class)
    public AuthenticationManager authManager(
            Optional<JwtAuthProvider> jwtAuthProvider,
            Optional<ApiKeyAuthProvider> apiKeyAuthProvider) {
        List<AuthenticationProvider> providers = List.of(jwtAuthProvider, apiKeyAuthProvider).stream()
                .<AuthenticationProvider>flatMap(Optional::stream)
                .toList();
        return new ProviderManager(providers);
    }

    @Configuration
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public static class DefaultSecurityFilterChainCfg {

        /**
         * Default {@link SecurityFilterChain} with all the standard repetitive configuration so that we don't have to
         * set it up each time from scratch in each microservice. Not only does this save effort but also helps to
         * avoid mistakes in security configuration.
         * <p>
         * Here we set up actuator access, no sessions, and filters for tenant uuid handling. Filters for
         * authentication are supposed to be plugged in via {@link SecurityFilterChainHttpConfigurer} and fine-grained
         * access control for endpoints is supposed to be done via {@link SecurityFilterChainAuthorizationConfigurer}.
         */
        @Bean
        public SecurityFilterChain defaultSecurityFilterChain(
                HttpSecurity http,
                TenantUuidExtractionFilter tenantUuidExtractionFilter,
                TenantVerificationFilter tenantVerificationFilter,
                List<SecurityFilterChainHttpConfigurer> httpConfigurers,
                List<SecurityFilterChainAuthorizationConfigurer> authConfigurers) throws Exception {
            if (isEmpty(httpConfigurers)) {
                // This is protection against a misconfiguration (both auth.jwt.enabled and auth.api-key.enabled are
                // false). Often people leave some toggle that disables auth completely for easier testing during
                // development. In my opinion, this is a very bad idea to have such a toggle that potentially can be
                // turned on in production. Plus it makes bugs in auth code to be more likely detected later.
                // It's better to automate (scripts, integration tests) tedious setup and use it during development
                // instead of bypassing auth completely.
                throw new IllegalStateException("no security configurers provided, we can't run with auth disabled");
            }
            if (isEmpty(authConfigurers)) {
                log.warn("no authZ configurers provided, most likely this is a misconfiguration unless this service " +
                        "does not expose a rest api");
            }
            var configuredHttp = http.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> {
                        var configuredAuth = auth
                                .requestMatchers("/error").permitAll()
                                // this is not exposed to the public but accessible without auth inside the cluster so
                                // that kubernetes can monitor app instances state
                                .requestMatchers(GET, "/actuator/health").permitAll()
                                .requestMatchers(GET, "/actuator/**").hasAuthority(ACTUATOR_READ);
                        for (var configurer : authConfigurers) {
                            configuredAuth = configurer.configure(configuredAuth);
                        }
                        // and finally, deny any non-matching requests
                        configuredAuth.anyRequest().denyAll();
                    })
                    .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                    .addFilterBefore(tenantVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(tenantUuidExtractionFilter, TenantVerificationFilter.class);
            for (var configurer : httpConfigurers) {
                configuredHttp = configurer.configure(configuredHttp);
            }
            return configuredHttp.build();
        }
    }
}
