package dev.nautchkafe.countdown.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

import java.time.Instant;

/**
 * Triggered when the countdown finishes successfully (i.e., all phases have completed).
 */
public final class CountdownFinishEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    private final String countdownId;
    private final Instant finishTime;
    private boolean cancelled;

    public CountdownFinishEvent(final String countdownId, final Instant finishTime) {
        this.countdownId = countdownId;
        this.finishTime = finishTime;
        this.cancelled = false;
    }

    public String getCountdownId() {
        return countdownId;
    }

    public Instant getFinishTime() {
        return finishTime;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}