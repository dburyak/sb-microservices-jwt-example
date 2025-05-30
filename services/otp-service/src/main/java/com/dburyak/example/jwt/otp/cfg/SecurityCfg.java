package com.dburyak.example.jwt.otp.cfg;

import com.dburyak.example.jwt.lib.auth.SecurityFilterChainAuthorizationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_REGISTERED_OTP_BY_CODE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_REGISTERED_OTP_BY_TYPE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;

@Configuration
public class SecurityCfg {

    @Bean
    public SecurityFilterChainAuthorizationConfigurer accessCfg() {
        return auth -> auth
                // This is a shortcut way to avoid actual email delivery: after making a call that generates an OTP,
                // the client can just call this endpoint to get the OTP code. In a real application, we would
                // never expose OTPs like this.
                .requestMatchers(GET, PATH_REGISTERED_OTP_BY_TYPE).permitAll()

                // Only authenticated users can claim OTPs. In case of a call from an unregistered/unauthenticated
                // user, a service will make a call on its behalf (with a service token).
                .requestMatchers(DELETE, PATH_REGISTERED_OTP_BY_CODE).authenticated();
    }
}
