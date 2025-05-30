package com.dburyak.example.jwt.otp.service.converter;

import com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP;
import org.springframework.stereotype.Component;

@Component
public class OTPConverter {

    public OTPType toDomain(com.dburyak.example.jwt.api.internal.otp.OTPType type) {
        return OTPType.valueOf(type.name());
    }

    public String toDomainEmail(String reqEmail) {
        return reqEmail != null ? reqEmail.strip().toLowerCase() : null;
    }

    public com.dburyak.example.jwt.api.internal.otp.OTPType toApiModel(OTPType type) {
        return com.dburyak.example.jwt.api.internal.otp.OTPType.valueOf(type.name());
    }

    public RegisteredUserOTP toApiModel(OTP otp) {
        return RegisteredUserOTP.builder()
                .uuid(otp.getUuid())
                .createdBy(otp.getCreatedBy())
                .createdDate(otp.getCreatedDate())
                .lastModifiedBy(otp.getLastModifiedBy())
                .lastModifiedDate(otp.getLastModifiedDate())
                .userUuid(otp.getUserUuid())
                .deviceId(otp.getDeviceId())
                .externalId(otp.getExternalId())
                .type(toApiModel(otp.getType()))
                .code(otp.getCode())
                .expiresAt(otp.getExpiresAt())
                .build();
    }
}
