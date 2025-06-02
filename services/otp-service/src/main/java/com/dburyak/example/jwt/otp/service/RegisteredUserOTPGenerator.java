package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.otp.cfg.AllOTPProperties;
import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

// NOTE: ideally, we should refactor RegisteredUserOTPGenerator and ExternallyIdentifiedOTPGenerator to not have code
// duplication. I just don't want to waste time on it.
@Component
@Log4j2
public class RegisteredUserOTPGenerator {
    private final String defaultSymbols;
    private final int defaultLength;
    private final String passwordResetSymbols;
    private final int passwordResetLength;

    private final RandomStringUtils rndUtils = RandomStringUtils.secure();

    public RegisteredUserOTPGenerator(AllOTPProperties props) {
        defaultSymbols = props.getDefaultCfg().getSymbols();
        defaultLength = props.getDefaultCfg().getLength();
        passwordResetSymbols = StringUtils.isNotBlank(props.getPasswordReset().getSymbols())
                ? props.getPasswordReset().getSymbols()
                : defaultSymbols;
        passwordResetLength = props.getPasswordReset().getLength() > 0
                ? props.getPasswordReset().getLength()
                : defaultLength;
    }

    public String generate(UUID tenantUuid, RegisteredUserOTP.Type type, String locale) {
        // Here we can decide which symbols to use, length, etc. depending on tenant, type, locale, etc.
        // In a real app this config would be stored in DB so each tenant can tweak it independently.
        // For simplicity, we use only static configuration here.
        return switch (type) {
            case CHANGE_PASSWORD -> rndUtils.next(passwordResetLength, passwordResetSymbols);
            // Add more cases for other OTP types if needed
            default -> {
                log.warn("unknown OTP type, using default configuration: type={}", type);
                yield rndUtils.next(defaultLength, defaultSymbols);
            }
        };
    }
}
