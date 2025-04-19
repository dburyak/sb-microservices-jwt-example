package com.dburyak.example.jwt.api.internal.common.msg;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@RequiredArgsConstructor(access = PROTECTED)
public abstract class TopicCfgProps {
    protected final String topicName;
    // depending on the messaging implementation, topic name and subscription name may have different meanings/names
    protected final String subscriptionName;
    protected final String consumerGroup;
    protected final Transport transport;
}
