package com.dburyak.example.jwt.lib.req.cfg;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.lib.req.TenantIdExtractionFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RequestAutoConfiguration {

    @Bean
    public RequestUtil requestUtil() {
        return new RequestUtil();
    }

    @Bean
    public TenantIdExtractionFilter tenantIdExtractionFilter(RequestUtil requestUtil) {
        return new TenantIdExtractionFilter(requestUtil);
    }
}
