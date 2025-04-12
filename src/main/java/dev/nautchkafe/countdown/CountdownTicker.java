package dev.nautchkafe.countdown;

/**
 * A functional interface representing a countdown ticker action.
 * This interface is intended to be implemented by lambda expressions or method references
 * that wish to perform some action at each tick of a countdown timer.
 */
@FunctionalInterface
interface CountdownTicker {

    /**
     * Performs this operation given a countdown context.
     *
     * @param countdownId the unique identifier for the countdown event
     * @param secondsLeft the number of seconds remaining in the countdown
     * @param ticksLeft the number of ticks left in the countdown
     */
    void apply(final String countdownId, final long secondsLeft, final long ticksLeft);
}