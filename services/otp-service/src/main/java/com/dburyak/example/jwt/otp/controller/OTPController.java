package com.dburyak.example.jwt.otp.controller;

import com.dburyak.example.jwt.api.common.PathParams;
import com.dburyak.example.jwt.api.common.QueryParams;
import com.dburyak.example.jwt.api.internal.otp.OTP;
import com.dburyak.example.jwt.api.internal.otp.OTPType;
import com.dburyak.example.jwt.lib.err.NotFoundException;
import com.dburyak.example.jwt.lib.req.Attributes;
import com.dburyak.example.jwt.otp.service.OTPService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.internal.otp.Paths.OTPS_BY_TYPE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.OTPS_ROOT;
import static com.dburyak.example.jwt.api.user.Paths.DEVICES_ROOT;
import static com.dburyak.example.jwt.api.user.Paths.DEVICE_BY_ID;
import static com.dburyak.example.jwt.api.user.Paths.USERS_ROOT;
import static com.dburyak.example.jwt.api.user.Paths.USER_BY_UUID;

/**
 * IMPORTANT: in a real application, we would not expose OTPs directly like this. We are making it
 * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
 * to substitute actual email delivery.
 */
@RestController
@RequestMapping(USERS_ROOT + USER_BY_UUID + DEVICES_ROOT + DEVICE_BY_ID + OTPS_ROOT + OTPS_BY_TYPE)
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;

    @GetMapping
    public ResponseEntity<OTP> getByUserUuidAndDeviceIdAndType(
            @RequestAttribute(Attributes.TENANT_UUID) @NotNull UUID callerTenantUuid,
            @RequestParam(name = QueryParams.TENANT_UUID, required = false) UUID requestedTenantUuid,
            @PathVariable(PathParams.USER_UUID) @NotNull UUID userUuid,
            @PathVariable(PathParams.DEVICE_ID) @NotBlank String deviceId,
            @PathVariable(com.dburyak.example.jwt.api.internal.otp.PathParams.OTP_TYPE) @NotNull OTPType type) {
        var t = requestedTenantUuid != null ? requestedTenantUuid : callerTenantUuid;
        var otp = otpService.findByTenantUuidAndUserUuidAndDeviceIdAndType(t, userUuid, deviceId, type);
        if (otp == null) {
            throw new NotFoundException("OTP(tenantUuid=%s, userUuid=%s, deviceId=%s)".formatted(
                    t, userUuid, deviceId));
        }
        return ResponseEntity.ok(otp);
    }
}
