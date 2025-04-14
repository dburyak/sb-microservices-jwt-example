package com.dburyak.example.jwt.lib.msg.redis;

import com.dburyak.example.jwt.lib.msg.Msg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MsgRedisImpl<T> implements Msg<T> {
    private final UUID msgId;
    private final Map<String, String> headers;
    private final T data;

    @Override
    public void ack() {
        // redis is not durable, there's no ack/nack mechanism
    }

    @Override
    public void nack() {
        // redis is not durable, there's no ack/nack mechanism
    }
}
