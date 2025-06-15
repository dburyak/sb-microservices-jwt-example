package com.dburyak.example.jwt.otp.controller.components;

import com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.lib.req.AbstractEnumConverter;
import org.springframework.stereotype.Component;

@Component
public class AnonymousOTPTypeEnumConverter extends AbstractEnumConverter<ExternallyIdentifiedOTP.Type> {

    public AnonymousOTPTypeEnumConverter() {
        super(ExternallyIdentifiedOTP.Type.class);
    }
}
