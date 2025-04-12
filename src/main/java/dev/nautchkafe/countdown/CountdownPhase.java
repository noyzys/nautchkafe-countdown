package dev.nautchkafe.countdown;

import java.time.Duration;

/**
 * A record representing a countdown phase, which includes a specific duration
 * and a countdown ticker that can be used to track the time.
 */
record CountdownPhase(
    Duration duration,
    CountdownTicker ticker
) {
}
