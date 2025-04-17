package com.dburyak.example.jwt.api.internal.tenant.cfg;

import com.dburyak.example.jwt.api.internal.tenant.TenantServiceClientInternal;
import com.dburyak.example.jwt.lib.auth.ServiceTokenManager;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {TenantApiInternalPropsAutoConfiguration.class})
@ConditionalOnProperty(prefix = "service-client.tenant", name = "enabled", havingValue = "true",
        matchIfMissing = false)
public class TenantServiceClientAutoConfiguration {

    @Bean
    public TenantServiceClientInternal tenantServiceClientInternal(TenantServiceClientProperties props,
            ServiceTokenManager serviceTokenManager) {
        return new TenantServiceClientInternal();
    }
}
