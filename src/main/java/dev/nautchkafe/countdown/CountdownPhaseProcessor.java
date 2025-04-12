package dev.nautchkafe.countdown;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@code CountdownPhaseProcessor} class is responsible for processing the phases of a countdown.
 * Each phase has a specific duration, and the countdown progresses through these phases.
 * 
 * <p>This final class is designed for efficient countdown handling with asynchronous task scheduling.</p>
 */
final class CountdownPhaseProcessor {
    
    private final CountdownScheduler scheduler;

    /**
     * Constructs a {@code CountdownPhaseProcessor} with the specified scheduler.
     *
     * @param scheduler the scheduler used for running tasks asynchronously.
     */
    CountdownPhaseProcessor(final CountdownScheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Processes all phases in the countdown associated with the given countdown ID.
     * 
     * <p>Each countdown phase is executed until completion according to its duration.</p>
     *
     * @param countdownId the identifier of the countdown being processed.
     * @param iterator an iterator over countdown phases to be processed.
     * @throws InterruptedException if the thread is interrupted while waiting for a countdown phase to complete.
     */
    void processPhases(final String countdownId, final Iterator<CountdownPhase> iterator) throws InterruptedException {
        while (iterator.hasNext()) {
            final CountdownPhase phase = iterator.next();
            runPhase(countdownId, phase);
        }
    }

    /**
     * Executes a single countdown phase given by the specified countdown ID.
     * 
     * <p>The phase's countdown is asynchronously ticked down until completion.</p>
     *
     * @param countdownId the identifier of the countdown phase.
     * @param phase the countdown phase to be executed.
     * @throws InterruptedException if the current thread is interrupted while waiting for the countdown phase to complete.
     */
    private void runPhase(final String countdownId, final CountdownPhase phase) throws InterruptedException {
        final long ticks = phase.duration().toMillis() / 50;
        final AtomicInteger counter = new AtomicInteger((int) ticks);
        final CountDownLatch latch = new CountDownLatch(1);

        scheduler.runTaskTimerAsynchronously(() -> {
            final int left = counter.decrementAndGet();
            phase.ticker().apply(countdownId, left / 20, left);

            if (left <= 0) {
                latch.countDown();
            }

        }, 0L, 1L);

        latch.await();
    }
}

