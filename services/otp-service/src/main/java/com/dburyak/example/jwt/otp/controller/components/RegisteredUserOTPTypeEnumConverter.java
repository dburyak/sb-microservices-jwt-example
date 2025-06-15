package com.dburyak.example.jwt.otp.controller.components;

import com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP;
import com.dburyak.example.jwt.lib.req.AbstractEnumConverter;
import org.springframework.stereotype.Component;

@Component
public class RegisteredUserOTPTypeEnumConverter extends AbstractEnumConverter<RegisteredUserOTP.Type> {

    public RegisteredUserOTPTypeEnumConverter() {
        super(RegisteredUserOTP.Type.class);
    }
}
