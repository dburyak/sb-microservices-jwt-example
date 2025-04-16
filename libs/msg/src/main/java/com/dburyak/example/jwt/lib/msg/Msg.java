package com.dburyak.example.jwt.lib.msg;

import com.dburyak.example.jwt.lib.auth.AppAuthentication;

import java.util.Map;

public interface Msg<T> {
    AppAuthentication getAuth();

    Map<String, String> getHeaders();

    T getData();

    // both ack and nack implementations must be idempotent, they may be called multiple times
    // moreover, the first call should be determinant, and any subsequent calls should be ignored
    // for example, we call "ack", and then "nack" - the second call should be ignored

    void ack();

    void nack();
}
