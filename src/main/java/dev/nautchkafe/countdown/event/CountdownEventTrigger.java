package dev.nautchkafe.countdown.event;

import org.bukkit.Server;

/**
 * The {@code CountdownEventTrigger} class provides utility methods to trigger events within a server.
 * This class should not be instantiated as it contains only static methods.
 */
public final class CountdownEventTrigger {

    private CountdownEventTrigger() {
    }

    /**
     * Triggers the specified {@link Event} using the server's plugin manager.
     * 
     * @param <TYPE> The type parameter which indicates the class type of the event.
     * @param server The server on which the event is triggered.
     * @param event The event to be triggered.
     */
    public static <TYPE extends Event> void triggerEvent(final Server server, final TYPE event) {
        server.getPluginManager().callEvent(event);
    }

    /**
     * Triggers the specified {@link Event} using the server's plugin manager, 
     * after executing a pre-event action.
     * 
     * @param <TYPE> The type parameter which indicates the class type of the event.
     * @param server The server on which the event is triggered.
     * @param event The event to be triggered.
     * @param preEventAction The action to be executed before the event is triggered.
     */
    public static <TYPE extends Event> void triggerEvent(final Server server, final TYPE event, final Consumer<TYPE> preEventAction) {
        preEventAction.accept(event);
        server.getPluginManager().callEvent(event);
    }

    /**
     * Triggers the specified {@link Cancellable} {@link Event} using the server's plugin manager.
     * If the event is cancelled during processing, it returns the cancelled event.
     * 
     * @param <TYPE> The type parameter which indicates the class type of the event.
     * @param server The server on which the event is triggered.
     * @param event The cancellable event to be triggered.
     * @return The event object, whether or not it was canceled.
     */
    public static <TYPE extends Event & Cancellable> TYPE triggerEvent(final Server server, final TYPE event) {
        server.getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return event;
        }

        return event;
    }

    /**
     * Creates a {@link Consumer} that, when executed, will trigger the given event on a server.
     * 
     * @param <TYPE> The type parameter which indicates the class type of the event.
     * @param event The event to be triggered.
     * @return A consumer that triggers the given event when executed with a server.
     */
    public static <TYPE extends Event> Consumer<Server> createEventTrigger(final TYPE event) {
        return server -> triggerEvent(server, event);
    }
}