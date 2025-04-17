package com.dburyak.example.jwt.api.internal.auth.cfg;

import com.dburyak.example.jwt.api.internal.auth.ApiKeyAuthProvider;
import com.dburyak.example.jwt.api.internal.auth.AuthServiceClientInternal;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class ApiKeyAuthProviderAutoConfiguration {

    @Bean
    public ApiKeyAuthProvider apiKeyAuthProvider(AuthServiceClientInternal authServiceClientInternal) {
        return new ApiKeyAuthProvider(authServiceClientInternal);
    }
}
