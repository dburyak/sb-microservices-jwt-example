package com.dburyak.example.jwt.api.internal.user.cfg;

import com.dburyak.example.jwt.api.internal.user.UserServiceClient;
import com.dburyak.example.jwt.lib.req.RequestUtil;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = {PropsAutoConfiguration.class})
@ConditionalOnProperty(name = "service-client.user.enabled", havingValue = "true", matchIfMissing = true)
public class ClientAutoConfiguration {

    @Bean
    public UserServiceClient userServiceClient(RequestUtil requestUtil, UserServiceClientProperties props) {
        return new UserServiceClient(requestUtil, props);
    }
}
