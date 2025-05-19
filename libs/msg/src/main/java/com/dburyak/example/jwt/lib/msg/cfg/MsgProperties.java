package com.dburyak.example.jwt.lib.msg.cfg;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "msg")
@Validated
@Value
@NonFinal
public class MsgProperties {
    boolean enabled;
    ExecType execType;
    int minThreads;

    /**
     * Maximum number of threads in the pool. Keep in mind that due to redis protocol history and dominance of the
     * blocking style in java, each subscription (call to
     * {@link com.dburyak.example.jwt.lib.msg.PointToPointMsgQueue#subscribe}) consumes one thread for listening to
     * a redis pubsub channel. Hence, the maximum number of threads in the pool should be at least equal to the number
     * of topics the app subscribes to, plus 1. For example, if the app subscribes to 3 topics, then the
     * {@code maxThreads} should be at least 4. In this case, only one thread left will be used for actual processing of
     * the messages. Optimal number should be around {@code numOfTopics + numOfCpus + 1}.
     */
    int maxThreads;

    @ConstructorBinding
    public MsgProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("VIRTUAL") @NotNull ExecType execType,
            @DefaultValue("3") @Min(2) int minThreads,
            @Positive Integer maxThreads) {
        if (maxThreads != null && maxThreads < minThreads) {
            throw new IllegalArgumentException("maxThreads must be greater than or equal to minThreads: minThreads=" +
                    minThreads + ", maxThreads=" + maxThreads);
        }
        if (maxThreads == null) {
            maxThreads = 4 + Runtime.getRuntime().availableProcessors(); // 3 topics as a reasonable default + 1
        }
        this.enabled = enabled;
        this.execType = execType;
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
    }

    public enum ExecType {
        OS_THREAD,
        VIRTUAL;
    }
}
