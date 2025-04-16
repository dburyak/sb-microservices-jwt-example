package com.dburyak.example.jwt.lib.msg.redis;

import com.dburyak.example.jwt.lib.auth.AppAuthentication;
import com.dburyak.example.jwt.lib.msg.Msg;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class MsgRedisImpl<T> implements Msg<T> {
    private final AppAuthentication auth;
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

    public MsgRedisImpl(UUID msgId, Map<String, String> headers, T data) {
        this.auth = null;
        this.msgId = msgId;
        this.headers = headers;
        this.data = data;
    }

    public MsgRedisImpl<T> withAuth(AppAuthentication auth) {
        return new MsgRedisImpl<>(auth, msgId, headers, data);
    }
}
