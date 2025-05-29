package com.dburyak.example.jwt.otp.cfg;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@ConfigurationProperties(prefix = "otp")
@Validated
@Value
public class AllOTPProperties {
    DefaultOTPProperties defaultCfg;
    EmailRegistrationOTPProperties emailRegistration;
    PasswordResetOTPProperties passwordReset;

    @ConstructorBinding
    public AllOTPProperties(
            @DefaultValue DefaultOTPProperties defaultCfg,
            @DefaultValue EmailRegistrationOTPProperties emailRegistration,
            @DefaultValue PasswordResetOTPProperties passwordReset) {
        this.defaultCfg = defaultCfg;
        this.emailRegistration = emailRegistration;
        this.passwordReset = passwordReset;
    }

    @Value
    @NonFinal
    @EqualsAndHashCode(callSuper = true)
    public static class DefaultOTPProperties extends OTPProperties {

        @ConstructorBinding
        public DefaultOTPProperties(
                @DefaultValue("1h") @DurationMin(seconds = 1) Duration ttl,
                @DefaultValue("6") @Min(4) int length,
                @DefaultValue("0123456789") @NotBlank String symbols) {
            super(ttl, length, symbols);
        }
    }

    @Value
    @NonFinal
    @EqualsAndHashCode(callSuper = true)
    public static class EmailRegistrationOTPProperties extends OTPProperties {

        @ConstructorBinding
        public EmailRegistrationOTPProperties(
                @DurationMin(seconds = 1) Duration ttl,
                @Min(4) int length,
                String symbols) {
            super(ttl, length, symbols);
        }
    }

    @Value
    @NonFinal
    @EqualsAndHashCode(callSuper = true)
    public static class PasswordResetOTPProperties extends OTPProperties {

        @ConstructorBinding
        public PasswordResetOTPProperties(
                @DurationMin(seconds = 1) Duration ttl,
                @Min(4) int length,
                String symbols) {
            super(ttl, length, symbols);
        }
    }
}
