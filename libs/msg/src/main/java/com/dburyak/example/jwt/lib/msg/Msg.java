package com.dburyak.example.jwt.lib.msg;

import java.util.Map;

public interface Msg<T> {
    Map<String, String> getHeaders();
    T getData();
    void ack();
    void nack();
}
