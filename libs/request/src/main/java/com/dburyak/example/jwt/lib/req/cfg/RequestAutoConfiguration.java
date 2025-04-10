package com.dburyak.example.jwt.lib.req.cfg;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.lib.req.TenantUuidHeaderExtractionFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RequestAutoConfiguration {

    @Bean
    public RequestUtil requestUtil() {
        return new RequestUtil();
    }

    @Bean
    public TenantUuidHeaderExtractionFilter tenantUuidHeaderExtractionFilter(RequestUtil requestUtil) {
        return new TenantUuidHeaderExtractionFilter(requestUtil);
    }
}
