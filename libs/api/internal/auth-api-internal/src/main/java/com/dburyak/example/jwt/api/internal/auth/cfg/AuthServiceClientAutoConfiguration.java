package com.dburyak.example.jwt.api.internal.auth.cfg;

import com.dburyak.example.jwt.api.internal.auth.AuthServiceClientInternal;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {AuthServiceClientPropsAutoConfiguration.class})
@ConditionalOnProperty(prefix = "service-client.auth", name = "enabled", havingValue = "true",
        matchIfMissing = false)
public class AuthServiceClientAutoConfiguration {

    @Bean
    public AuthServiceClientInternal authServiceClient(AuthServiceClientProperties props,
            ServiceTokenManager serviceTokenManager) {
        return new AuthServiceClientInternal(props, serviceTokenManager);
    }
}
