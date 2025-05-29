package com.dburyak.example.jwt.otp.cfg;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PROTECTED)
public abstract class OTPProperties {
    private final Duration ttl;
    private final int length;
    private final String symbols;
}
