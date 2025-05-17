package com.dburyak.example.jwt.lib.req.cfg;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.lib.req.TenantUuidExtractionFilter;
import com.dburyak.example.jwt.lib.req.TenantVerificationFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RequestAutoConfiguration {

    @Bean
    public RequestUtil requestUtil() {
        return new RequestUtil();
    }

    @Bean
    public TenantUuidExtractionFilter tenantUuidHeaderExtractionFilter(RequestUtil requestUtil) {
        return new TenantUuidExtractionFilter(requestUtil);
    }

    @Bean
    public TenantVerificationFilter tenantVerificationFilter(RequestUtil requestUtil) {
        return new TenantVerificationFilter(requestUtil);
    }
}
