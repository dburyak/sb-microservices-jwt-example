package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.otp.cfg.AllOTPProperties;
import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Log4j2
public class ExternallyIdentifiedOTPGenerator {
    private final String defaultSymbols;
    private final int defaultLength;
    private final String passwordResetSymbols;
    private final int passwordResetLength;
    private final String registrationWithEmailSymbols;
    private final int registrationWithEmailLength;

    private final RandomStringUtils rndUtils = RandomStringUtils.secure();

    public ExternallyIdentifiedOTPGenerator(AllOTPProperties props) {
        defaultSymbols = props.getDefaultCfg().getSymbols();
        defaultLength = props.getDefaultCfg().getLength();
        passwordResetSymbols = props.getPasswordReset().getSymbols() != null
                ? props.getPasswordReset().getSymbols()
                : defaultSymbols;
        passwordResetLength = props.getPasswordReset().getLength() > 0
                ? props.getPasswordReset().getLength()
                : defaultLength;
        registrationWithEmailSymbols = props.getRegistrationWithEmail().getSymbols() != null
                ? props.getRegistrationWithEmail().getSymbols()
                : defaultSymbols;
        registrationWithEmailLength = props.getRegistrationWithEmail().getLength() > 0
                ? props.getRegistrationWithEmail().getLength()
                : defaultLength;
    }

    public String generate(UUID tenantUuid, ExternallyIdentifiedOTP.Type type, String locale) {
        // Here we can decide which symbols to use, length, etc. depending on tenant, type, locale, etc.
        // In a real app this config would be stored in DB so each tenant can tweak it independently.
        // For simplicity, we use only static configuration here.
        return switch (type) {
            case PASSWORD_RESET -> rndUtils.next(passwordResetLength, passwordResetSymbols);
            case REGISTRATION_WITH_EMAIL -> rndUtils.next(registrationWithEmailLength, registrationWithEmailSymbols);
            // Add more cases for other OTP types if needed
            default -> {
                log.warn("unknown OTP type, using default configuration: type={}", type);
                yield rndUtils.next(defaultLength, defaultSymbols);
            }
        };
    }
}
