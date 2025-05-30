package com.dburyak.example.jwt.otp.controller;

import com.dburyak.example.jwt.api.internal.otp.RegisteredUserOTP;
import com.dburyak.example.jwt.otp.err.RegistereUserOTPNotFoundException;
import com.dburyak.example.jwt.otp.service.RegisteredUserOTPService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.common.PathParams.DEVICE_ID;
import static com.dburyak.example.jwt.api.common.PathParams.USER_UUID;
import static com.dburyak.example.jwt.api.internal.otp.PathParams.OTP_CODE;
import static com.dburyak.example.jwt.api.internal.otp.PathParams.OTP_TYPE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.OTP_BY_CODE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.OTP_BY_TYPE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_REGISTERED_OTPS;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(PATH_REGISTERED_OTPS)
@RequiredArgsConstructor
public class RegisteredUsersOTPController {
    private final RegisteredUserOTPService otpService;

    /**
     * IMPORTANT: in a real application, we would not expose OTPs directly like this. We are making it
     * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
     * to substitute actual email delivery. Only the "claim" endpoint is valid for real applications.
     */
    @GetMapping(OTP_BY_TYPE)
    public ResponseEntity<RegisteredUserOTP> getByUserUuidAndDeviceIdAndType(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @PathVariable(USER_UUID) @NotNull UUID userUuid,
            @PathVariable(DEVICE_ID) @NotBlank String deviceId,
            @PathVariable(OTP_TYPE) @NotNull RegisteredUserOTP.Type type) {
        var otp = otpService.findByTenantUuidAndUserUuidAndDeviceIdAndType(tenantUuid, userUuid, deviceId, type);
        if (otp == null) {
            throw new RegistereUserOTPNotFoundException(userUuid, deviceId, type);
        }
        return ResponseEntity.ok(otp);
    }

    @DeleteMapping(OTP_BY_TYPE + OTP_BY_CODE)
    public ResponseEntity<Void> claimByUserUuidAndDeviceIdAndTypeAndCode(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @PathVariable(USER_UUID) @NotNull UUID userUuid,
            @PathVariable(DEVICE_ID) @NotBlank String deviceId,
            @PathVariable(OTP_TYPE) @NotNull RegisteredUserOTP.Type type,
            @PathVariable(OTP_CODE) @NotBlank String otpCode) {
        var otp = otpService.claimByTenantUuidAndUserUuidAndDeviceIdAndTypeAndCode(tenantUuid, userUuid, deviceId, type,
                otpCode);
        if (otp == null) {
            throw new RegistereUserOTPNotFoundException(userUuid, deviceId, type, otpCode);
        }
        return ResponseEntity.noContent().build();
    }
}
