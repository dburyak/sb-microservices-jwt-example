package com.dburyak.example.jwt.otp.service;

import com.dburyak.example.jwt.otp.domain.OTPType;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OTPGenerator {
    private static final String SYMBOLS = "0123456789";
    private static final int DEFAULT_LENGTH = 6;
    private final RandomStringUtils rndUtils = RandomStringUtils.secure();

    public String generate(UUID tenantUuid, OTPType type, String locale) {
        // here we can decide which symbols to use, length, etc. per tenant and otp type
        return rndUtils.next(DEFAULT_LENGTH, SYMBOLS);
    }
}
