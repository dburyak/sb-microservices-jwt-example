package com.dburyak.example.jwt.api.internal.otp.cfg;

import com.dburyak.example.jwt.api.internal.otp.OTPServiceClientInternal;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {PropsAutoConfiguration.class})
@ConditionalOnProperty(name = "service-client.otp.enabled", havingValue = "true", matchIfMissing = true)
public class ClientAutoConfiguration {

    @Bean
    public OTPServiceClientInternal otpServiceClient(RequestUtil requestUtil, OTPServiceClientProperties props) {
        return new OTPServiceClientInternal(requestUtil, props);
    }
}
