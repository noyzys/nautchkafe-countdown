package dev.nautchkafe.countdown;

import org.bukkit.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@code CountdownTimer} class manages countdown tasks, allowing starts and cancellations.
 * It uses a {@link Server} and a {@link CountdownScheduler} to handle the countdown phases and events.
 */
final class CountdownTimer {

    private final Server server;
    private final CountdownScheduler scheduler;
    private final Map<String, CountdownTask> tasks = new ConcurrentHashMap<>();

    /**
     * Constructs a {@code CountdownTimer} with the specified server.
     * Initializes the scheduler for countdown management.
     * 
     * @param server the server context used by this countdown timer
     */
    CountdownTimer(final Server server) {
        this.server = server;
        this.scheduler = new CountdownScheduler(server);
    }

    /**
     * Starts a phased countdown task with the given phases and completion/cancel actions.
     * 
     * @param countdownId the unique identifier for the countdown task
     * @param phases the list of phases to execute in sequence during the countdown
     * @param onComplete the action to perform upon successful countdown completion
     * @param onCancel the action to perform if the countdown is canceled
     */
    public void startPhasedCountdown(final String countdownId,
            final List<CountdownPhase> phases, final CountdownCompletion onComplete, final CountdownCancel onCancel) {
        new CountdownDispatcher(scheduler, tasks, onComplete, onCancel).startPhasedCountdown(countdownId, phases);
    }

    /**
     * Cancels an ongoing countdown task identified by the given ID.
     * 
     * @param countdownId the unique identifier of the countdown task to cancel
     */
    public void cancel(final String countdownId) {
        new CountdownDispatcher(scheduler, tasks, null, null).cancel(countdownId);
    }
}