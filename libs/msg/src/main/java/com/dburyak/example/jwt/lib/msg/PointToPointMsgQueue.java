package com.dburyak.example.jwt.lib.msg;

import java.util.function.Consumer;

/**
 * Point-to-point message queue. One message is delivered to a single consumer in a group. For example, "tenant created"
 * event needs to be processed only by a single instance of user-service to create "admin" user for the tenant.
 */
public interface PointToPointMsgQueue<T> {
    String DEFAULT_CONSUMER_GROUP = "default";

    void publish(String topic, T msg);

    void subscribe(String topic, String consumerGroup, Consumer<Msg<T>> handler);

    default void subscribe(String topic, Consumer<Msg<T>> handler) {
        subscribe(topic, DEFAULT_CONSUMER_GROUP, handler);
    }
}
