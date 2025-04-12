package dev.nautchkafe.countdown;

/**
 * Functional interface to handle the cancellation of a countdown.
 * This interface defines the method to accept a countdown ID, indicating
 * that the countdown associated with the given ID should be cancelled.
 * 
 * <p>Implementations of this interface should provide the logic
 * to stop and clean any resources related to the countdown operation.</p>
 */
@FunctionalInterface
interface CountdownCancel {

    void accept(final String countdownId);
}