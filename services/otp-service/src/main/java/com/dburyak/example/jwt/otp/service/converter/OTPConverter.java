package com.dburyak.example.jwt.otp.service.converter;

import com.dburyak.example.jwt.lib.mongo.ExternalId;
import com.dburyak.example.jwt.otp.domain.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.otp.domain.RegisteredUserOTP;
import org.springframework.stereotype.Component;

@Component
public class OTPConverter {

    public RegisteredUserOTP.Type toDomain(com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP.Type type) {
        return RegisteredUserOTP.Type.valueOf(type.name());
    }

    public ExternallyIdentifiedOTP.Type toDomain(
            com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.Type type) {
        return ExternallyIdentifiedOTP.Type.valueOf(type.name());
    }

    public String toDomainEmail(String reqEmail) {
        return reqEmail != null ? reqEmail.strip().toLowerCase() : null;
    }

    public com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP.Type toApiModel(RegisteredUserOTP.Type type) {
        return com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP.Type.valueOf(type.name());
    }

    public com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.Type toApiModel(
            ExternallyIdentifiedOTP.Type type) {
        return com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.Type.valueOf(type.name());
    }

    public com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP toApiModel(RegisteredUserOTP otp) {
        return com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP.builder()
                .uuid(otp.getUuid())
                .createdBy(otp.getCreatedBy())
                .createdDate(otp.getCreatedDate())
                .lastModifiedBy(otp.getLastModifiedBy())
                .lastModifiedDate(otp.getLastModifiedDate())
                .deviceId(otp.getDeviceId())
                .code(otp.getCode())
                .expiresAt(otp.getExpiresAt())
                .userUuid(otp.getUserUuid())
                .type(toApiModel(otp.getType()))
                .build();
    }

    public com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP toApiModel(
            ExternallyIdentifiedOTP otp) {
        return com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP.builder()
                .uuid(otp.getUuid())
                .createdBy(otp.getCreatedBy())
                .createdDate(otp.getCreatedDate())
                .lastModifiedBy(otp.getLastModifiedBy())
                .lastModifiedDate(otp.getLastModifiedDate())
                .deviceId(otp.getDeviceId())
                .code(otp.getCode())
                .expiresAt(otp.getExpiresAt())
                .externalId(toApiModel(otp.getExternalId()))
                .type(toApiModel(otp.getType()))
                .build();
    }

    public ExternalId toDomain(com.dburyak.example.jwt.api.common.ExternalId externalId) {
        return new ExternalId(toDomainEmail(externalId.getEmail()));
    }

    public com.dburyak.example.jwt.api.common.ExternalId toApiModel(ExternalId externalId) {
        return new com.dburyak.example.jwt.api.common.ExternalId(externalId.getEmail());
    }
}
