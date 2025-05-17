package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.AuthoritiesMapper;
import com.dburyak.example.jwt.lib.auth.FilterRegistrationHttpConfigurer;
import com.dburyak.example.jwt.lib.auth.SecurityFilterChainHttpConfigurer;
import com.dburyak.example.jwt.lib.auth.apikey.ApiKeyAuthExtractor;
import com.dburyak.example.jwt.lib.auth.apikey.ApiKeyAuthProvider;
import com.dburyak.example.jwt.lib.auth.apikey.ApiKeyFilter;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;

@AutoConfiguration(after = PropsAutoConfiguration.class)
@ConditionalOnProperty(prefix = "auth.apikey", name = "enabled", havingValue = "true", matchIfMissing = false)
public class ApiKeyAuthAutoConfiguration {

    @Bean
    public ApiKeyAuthExtractor apiKeyAuthExtractor() {
        return new ApiKeyAuthExtractor();
    }

    @Bean
    public ApiKeyAuthProvider apiKeyAuthProvider(AuthoritiesMapper rolesMapper) {
        return new ApiKeyAuthProvider(rolesMapper);
    }

    @Bean
    public ApiKeyFilter apiKeyFilter(ApiKeyAuthExtractor apiKeyAuthExtractor, RequestUtil requestUtil,
            AuthenticationManager authManager) {
        return new ApiKeyFilter(apiKeyAuthExtractor, requestUtil, authManager);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChainHttpConfigurer apiKeyFilterConfigurer(ApiKeyFilter apiKeyFilter) {
        return new FilterRegistrationHttpConfigurer(apiKeyFilter, apiKeyFilter.getOrder());
    }
}
