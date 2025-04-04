package com.dburyak.example.jwt.lib.auth.cfg;

import com.dburyak.example.jwt.lib.auth.RequestUtil;
import com.dburyak.example.jwt.lib.auth.TenantIdExtractionFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class UtilsAutoConfiguration {

    @Bean
    public RequestUtil requestUtil() {
        return new RequestUtil();
    }

    @Bean
    public TenantIdExtractionFilter tenantIdExtractionFilter(RequestUtil requestUtil) {
        return new TenantIdExtractionFilter(requestUtil);
    }
}
