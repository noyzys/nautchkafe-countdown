package dev.nautchkafe.countdown;

import org.bukkit.Bukkit;

import java.util.List;
import java.util.Map;
import java.java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The CountdownDispatcher class manages and dispatches countdown tasks, each associated with a unique countdown ID.
 * This class allows for starting, cancelling, and querying countdown tasks. Each countdown is composed of multiple phases.
 */
final class CountdownDispatcher {
    
    private final CountdownScheduler scheduler;
    private final Map<String, CountdownTask> countdowns;
    private final CountdownCompletion onComplete;
    private final CountdownCancel onCancel;

    /**
     * Constructs a CountdownDispatcher with the specified scheduler, countdown tasks, completion handler,
     * and cancel handler.
     * 
     * @param scheduler   the scheduler to manage countdown phases
     * @param countdowns  a map to hold the countdown tasks, initially populated with no tasks
     * @param onComplete  callback triggered upon countdown completion
     * @param onCancel    callback triggered upon countdown cancellation
     */
    CountdownDispatcher(final CountdownScheduler scheduler,
            final Map<String, CountdownTask> countdowns, 
            final CountdownCompletion onComplete, 
            final CountdownCancel onCancel) {
        this.scheduler = scheduler;
        this.countdowns =  new ConcurrentHashMap<>();
        this.onComplete = onComplete;
        this.onCancel = onCancel;
    }

    /**
     * Starts a phased countdown for the given countdown ID and phases if not already running.
     * 
     * @param countdownId the unique identifier for the countdown
     * @param phases      the list of phases to execute
     */
    void startPhasedCountdown(final String countdownId, final List<CountdownPhase> phases) {
        if (countdowns.containsKey(countdownId)) {
            return;
        }

        final Runnable composed = compose(countdownId, phases.iterator());
        final CompletableFuture<Void> future = CompletableFuture.runAsync(composed);
        countdowns.put(countdownId, new CountdownTask(future, null, onCancel));

        if (!isCancelled(countdownId)) {
            final CountdownStartEvent startEvent = new CountdownStartEvent(countdownId);
            CountdownEventTrigger.triggerEvent(tickServer(), startEvent);
        }
    }

    /**
     * Composes the countdown task using the given countdown ID and phase iterator.
     * 
     * @param countdownId the unique identifier for the countdown
     * @param iterator    an iterator over the phases to execute
     * @return a Runnable representing the composed task
     */
    private Runnable compose(final String countdownId, final Iterator<CountdownPhase> iterator) {
        return () -> {
            try {
                new CountdownPhaseProcessor(scheduler).processPhases(countdownId, iterator);

                countdowns.remove(countdownId);
                scheduler.runAsync(() -> onComplete.accept(countdownId, Instant.now()));
                
                if (!isCancelled(countdownId)) {
                    final CountdownFinishEvent finishEvent = new CountdownFinishEvent(countdownId, Instant.now());
                    CountdownEventTrigger.triggerEvent(tickServer(), finishEvent);
                }

                final CountdownCloseEvent closeEvent = new CountdownCloseEvent(countdownId);
                CountdownEventTrigger.triggerEvent(tickServer(), closeEvent);            
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }

    /**
     * Checks if a countdown task exists for the given ID.
     * 
     * @param id the countdown ID to check
     * @return true if the countdown exists, false otherwise
     */
    boolean hasCountdown(final String id) {
        return countdowns.containsKey(id);
    }

    /**
     * Cancels the countdown associated with the given ID, triggering cancellation events.
     * 
     * @param countdownId the unique identifier for the countdown
     */
    void cancel(final String countdownId) {
        final CountdownTask task = countdowns.remove(countdownId);
        if (task != null) {
            task.cancel(countdownId);
            
            if (!isCancelled(countdownId)) {
                final CountdownCancelEvent cancelEvent = new CountdownCancelEvent(countdownId);
                CountdownEventTrigger.triggerEvent(tickServer(), cancelEvent);
            }

            final CountdownCloseEvent closeEvent = new CountdownCloseEvent(countdownId);
            CountdownEventTrigger.triggerEvent(tickServer(), closeEvent);
        }
    }

    /**
     * Cancels all active countdowns.
     */
    void cancelAllCountdowns() {
        for (final String id : countdowns.keySet()) {
            cancel(id);
        }
    }

    /**
     * Checks if the countdown associated with the given ID is cancelled.
     * 
     * @param countdownId the unique identifier for the countdown
     * @return true if the countdown is cancelled, false otherwise
     */
    private boolean isCancelled(final String countdownId) {
        final CountdownTask task = countdowns.get(countdownId);
        return task != null && task.future().isCancelled();
    }

    /**
     * Returns the server instance used for triggering events.
     * 
     * @return the server instance
     */
    private Server tickServer() {
        return Bukkit.getServer();
    }
}
