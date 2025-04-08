package com.dburyak.example.jwt.lib.req.cfg;

import com.dburyak.example.jwt.lib.req.RequestUtil;
import com.dburyak.example.jwt.lib.req.TenantIdHeaderExtractionFilter;
import com.dburyak.example.jwt.lib.req.TenantIdQueryParamExtractionFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class RequestAutoConfiguration {

    @Bean
    public RequestUtil requestUtil() {
        return new RequestUtil();
    }

    @Bean
    public TenantIdHeaderExtractionFilter tenantIdHeaderExtractionFilter(RequestUtil requestUtil) {
        return new TenantIdHeaderExtractionFilter(requestUtil);
    }

    @Bean
    public TenantIdQueryParamExtractionFilter tenantIdQueryParamExtractionFilter(RequestUtil requestUtil) {
        return new TenantIdQueryParamExtractionFilter(requestUtil);
    }
}
