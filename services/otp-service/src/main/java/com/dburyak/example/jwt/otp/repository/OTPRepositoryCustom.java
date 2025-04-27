package com.dburyak.example.jwt.otp.repository;

import com.dburyak.example.jwt.otp.domain.OTP;

public interface OTPRepositoryCustom {
    OTP insertOrReplaceByTenantUuidAndUserUuidAndDeviceIdAndType(OTP otp);
}
