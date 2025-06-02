package com.dburyak.example.jwt.otp.controller;

import com.dburyak.example.jwt.api.common.ApiView.READ;
import com.dburyak.example.jwt.api.common.ApiView.UPDATE;
import com.dburyak.example.jwt.api.common.ExternalId;
import com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP;
import com.dburyak.example.jwt.otp.err.ExternallyIdentifiedOTPNotFoundException;
import com.dburyak.example.jwt.otp.service.ExternallyIdentifiedOTPService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.dburyak.example.jwt.api.common.Paths.DELETE_RESOURCE;
import static com.dburyak.example.jwt.api.common.Paths.GET_RESOURCE;
import static com.dburyak.example.jwt.api.common.QueryParams.DEVICE_ID;
import static com.dburyak.example.jwt.api.internal.otp.PathParams.OTP_CODE;
import static com.dburyak.example.jwt.api.internal.otp.PathParams.OTP_TYPE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.OTP_BY_CODE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.OTP_BY_TYPE;
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_ANONYMOUS_OTPS;
import static com.dburyak.example.jwt.lib.req.Attributes.TENANT_UUID;

@RestController
@RequestMapping(PATH_ANONYMOUS_OTPS)
@RequiredArgsConstructor
public class AnonymousUsersOTPController {
    private final ExternallyIdentifiedOTPService otpService;

    /**
     * IMPORTANT: in a real application, we would not expose OTPs directly like this. We are making it
     * accessible only because we won't be sending real emails with OTPs in this example app. This is a clumsy way
     * to substitute actual email delivery. Only the "claim" endpoint is valid for real applications.
     * <p>
     * NOTE: using POST+body instead of GET to avoid exposing email addresses in the URL. Semantically, this is a
     * "read" operation.
     */
    @PostMapping(OTP_BY_TYPE + GET_RESOURCE)
    @JsonView(READ.class)
    public ResponseEntity<ExternallyIdentifiedOTP> getByExternalIdAndDeviceIdAndType(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @RequestParam(DEVICE_ID) @NotBlank String deviceId,
            @PathVariable(OTP_TYPE) @NotNull ExternallyIdentifiedOTP.Type type,
            @RequestBody @Validated(READ.class) @NotNull ExternalId externalId) {
        var otp = otpService.findOTP(tenantUuid, externalId, deviceId, type);
        if (otp == null) {
            throw new ExternallyIdentifiedOTPNotFoundException(deviceId, type);
        }
        return ResponseEntity.ok(otp);
    }

    @PostMapping(OTP_BY_TYPE + OTP_BY_CODE + DELETE_RESOURCE)
    public ResponseEntity<Void> claimByExternalIdAndDeviceIdAndTypeAndCode(
            @RequestAttribute(TENANT_UUID) @NotNull UUID tenantUuid,
            @RequestParam(DEVICE_ID) @NotBlank String deviceId,
            @PathVariable(OTP_TYPE) @NotNull ExternallyIdentifiedOTP.Type type,
            @PathVariable(OTP_CODE) @NotBlank String otpCode,
            @RequestBody @Validated(UPDATE.class) @NotNull ExternalId externalId) {
        var otp = otpService.claimOTP(tenantUuid, externalId, deviceId, type, otpCode);
        if (otp == null) {
            throw new ExternallyIdentifiedOTPNotFoundException(deviceId, type, otpCode);
        }
        return ResponseEntity.noContent().build();
    }
}
