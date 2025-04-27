package com.dburyak.example.jwt.otp.service.converter;

import com.dburyak.example.jwt.otp.domain.OTPType;
import org.springframework.stereotype.Component;

@Component
public class OTPConverter {

    public OTPType toDomain(com.dburyak.example.jwt.api.internal.otp.OTPType type) {
        return OTPType.valueOf(type.name());
    }
}
