package com.dburyak.example.jwt.lib.msg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Slf4j
@RequiredArgsConstructor
public class LifecycleAwareExecutor implements Executor, DisposableBean {
    private final ExecutorService delegate;
    private final Duration awaitTasksTimeout;
    private final Duration interruptedTasksTimeout;

    @Override
    public void execute(Runnable command) {
        delegate.execute(command);
    }

    @Override
    public void destroy() throws Exception {
        if (delegate instanceof DisposableBean disposableDelegate) {
            disposableDelegate.destroy();
        } else {
            // "shutdown" does nothing to already running and scheduled tasks, but stops accepting any new tasks
            delegate.shutdown();
            var terminatedWithoutInterruption = delegate.awaitTermination(awaitTasksTimeout.toMillis(), MILLISECONDS);
            if (!terminatedWithoutInterruption) {
                // "shutdownNow" interrupts all running tasks and returns a list of tasks that were queued up
                // but not yet executed
                var notStartedTasks = delegate.shutdownNow();
                if (!notStartedTasks.isEmpty()) {
                    // In a real app, such a situation would be a serious problem as practically always it results in
                    // data loss unless special care is taken. And the exact approach depends on the use case.

                    // Specifically, when using this executor as a redis pub/sub subscriber, there's nothing we can do
                    // about it due to the nature of redis pub/sub. It's not durable, messages are not stored anywhere.
                    // So if we received a message and did not process it, it is lost forever. We should not use redis
                    // for messaging in the first place. Reminder: this example app is not about messaging, I picked
                    // redis for this ONLY because it is easy to install and use without any configuration.

                    // However, in a real app again, if such a situation ever happens (i.e. we have so many tasks queued
                    // up that we cannot process them within the graceful shutdown period), we should do two things:
                    // - use technology/pattern that prevents data loss in such a situation. For messaging, it should
                    //   be durable message queues that support the concept of messasge "acknowledgements"/"negative
                    //   acknowledgements" (e.g. RabbitMQ, Kafka, etc). For outgoing messages, it should be either of
                    //   "transactional outbox" or "event sourcing" patterns. For general activities, you should take
                    //   care of activity state persistence and recovery in case of loss.

                    log.error("executor still has unscheduled tasks after shutdown timeout (tasks are discarded): " +
                            "numTasks={}", notStartedTasks.size());
                }
                if (!delegate.awaitTermination(interruptedTasksTimeout.toMillis(), MILLISECONDS)) {
                    log.error("some interrupted tasks did not finish within timeout period");
                }
            }
        }
    }
}
