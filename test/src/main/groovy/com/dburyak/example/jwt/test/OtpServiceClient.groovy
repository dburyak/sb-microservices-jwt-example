package com.dburyak.example.jwt.test

import com.dburyak.example.jwt.api.common.ExternalId
import com.dburyak.example.jwt.api.internal.otp.ExternallyIdentifiedOTP

import static com.dburyak.example.jwt.api.common.QueryParams.DEVICE_ID
import static com.dburyak.example.jwt.api.internal.otp.Paths.PATH_ANONYMOUS_OTP_BY_TYPE
import static com.dburyak.example.jwt.test.BaseSpec.OTP_SERVICE_URL
import static org.springframework.http.MediaType.APPLICATION_JSON

class OtpServiceClient extends ServiceClient {

    OtpServiceClient(UUID tenantUuid = null, String jwtToken = null) {
        super(OTP_SERVICE_URL, tenantUuid, jwtToken)
    }

    @Override
    protected OtpServiceClient createWith(UUID tenantUuid = null, String jwtToken = null) {
        new OtpServiceClient(tenantUuid, jwtToken)
    }

    ExternallyIdentifiedOTP findExternallyIdentifiedOTP(String email, String deviceId,
            ExternallyIdentifiedOTP.Type type, UUID tenantUuid = null) {
        rest.post()
                .uri {
                    def builder = it.path(PATH_ANONYMOUS_OTP_BY_TYPE)
                            .queryParam(DEVICE_ID, deviceId)
                    if (tenantUuid) {
                        builder = builder.queryParam('tenantUuid', tenantUuid)
                    }
                    builder.build(type)
                }
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(new ExternalId(email))
                .retrieve()
                .body(ExternallyIdentifiedOTP)
    }
}
