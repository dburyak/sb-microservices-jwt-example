package com.dburyak.example.jwt.lib.msg;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Point-to-point message queue. One message is delivered to a single consumer in a group. For example, "tenant created"
 * event needs to be processed only by a single instance of user-service to create "admin" user for the tenant.
 * <p>
 * NOTE: realistically, we would also need to implement "unsubscribe" method to be able to shut down the application
 * gracefully. I don't want to fiddle around with it in this example since it's about JWT and auth, and not about
 * messaging.
 */
public interface PointToPointMsgQueue {
    String DEFAULT_CONSUMER_GROUP = "default";

    <T> void publish(String topic, T msg);

    <T> void subscribe(String topic, String consumerGroup, Predicate<Msg<T>> access,
            Consumer<Msg<T>> handler);

    default <T> void subscribe(String topic, Predicate<Msg<T>> access, Consumer<Msg<T>> handler) {
        subscribe(topic, DEFAULT_CONSUMER_GROUP, access, handler);
    }
}
