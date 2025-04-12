package dev.nautchkafe.countdown.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Cancellable;

/**
 * Triggered when the countdown is canceled before completion.
 */
public final class CountdownCancelEvent extends Event implements Cancellable {
    
    private static final HandlerList HANDLERS = new HandlerList();
    private final String countdownId;
    private boolean cancelled;

    public CountdownCancelEvent(final String countdownId) {
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