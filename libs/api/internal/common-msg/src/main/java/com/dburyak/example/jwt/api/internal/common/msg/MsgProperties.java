package com.dburyak.example.jwt.api.internal.common.msg;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PROTECTED)
public abstract class MsgProperties {
    private final String consumerGroup;
}
