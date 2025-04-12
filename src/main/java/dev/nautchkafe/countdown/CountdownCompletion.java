package dev.nautchkafe.countdown;

import java.time.Instant;

/**
 * Functional interface representing a callback to be invoked upon
 * the completion of a countdown.
 */
@FunctionalInterface
public interface CountdownCompletion {

    void accept(final String countdownId, final Instant endTime);
}