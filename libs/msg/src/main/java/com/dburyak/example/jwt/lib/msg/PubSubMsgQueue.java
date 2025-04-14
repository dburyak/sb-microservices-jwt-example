package com.dburyak.example.jwt.lib.msg;

import java.util.function.Consumer;

/**
 * Publish-subscribe message queue.
 * With this type of message queue, one message is delivered to all subscribers.
 * <p>
 * Examples of where it should be used:
 * <ul>
 *     <li>notification to update local caches - each instance of the same app should handle the message
 *     <li>notification to reload external data (some configuration for example) - each instance needs to reload the
 *     data
 * </ul>
 * Example of where it should NOT be used:
 * <ul>
 *     <li>tenant-service publishes "tenant created" event, user-service has to create "admin" user for the tenant.
 *     Only one instance of user-service should handle the event.
 * </ul>
 * NOTE: most likely, this type of message queue is not needed in this example application.
 */
public interface PubSubMsgQueue<T> {
    void publish(String topic, T msg);
    void subscribe(String topic, Consumer<Msg<T>> handler);
}
