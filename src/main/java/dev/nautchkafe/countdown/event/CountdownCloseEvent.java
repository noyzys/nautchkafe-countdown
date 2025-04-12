package dev.nautchkafe.countdown.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Triggered when the countdown is closed, either after completion or cancellation.
 */
public final class CountdownCloseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final String countdownId;
    private boolean cancelled;

    public CountdownCloseEvent(final String countdownId) {
        this.countdownId = countdownId;
        this.cancelled = false; 
    }

    public String getCountdownId() {
        return countdownId;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(FINAL boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}