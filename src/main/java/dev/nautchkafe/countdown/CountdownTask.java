package dev.nautchkafe.countdown;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

/**
 * A record representing a task for counting down to a specified end time.
 * It holds a future task and provides the ability to cancel the countdown.
 * Cancellation triggers the execution of a provided callback.
 */
record CountdownTask(
    CompletableFuture<Void> future,
    Instant endTime,
    CountdownCancel cancel
) {
    
    void cancel(final String countdownId) {
        future.cancel(true);
        cancel.accept(countdownId);
    }
}