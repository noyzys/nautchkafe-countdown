package dev.nautchkafe.countdown;

import org.bukkit.Server;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * The {@code CountdownScheduler} class is responsible for scheduling tasks 
 * to be run on the server asynchronously or periodically with an initial delay.
 * This class utilizes the Bukkit scheduling system to manage tasks.
 */
final class CountdownScheduler {

    private final CountdownPlugin plugin;
    private final Server server;
    private final BukkitScheduler scheduler;

    /**
     * Constructs a {@code CountdownScheduler} with the specified server 
     * and plugin. Initializes the scheduler using the server.
     *
     * @param server the server instance that provides the scheduler
     * @param plugin the plugin instance associated with this scheduler
     */
    CountdownScheduler(final Server server, final CountdownPlugin plugin) {
        this.server = server;
        this.plugin = plugin;
        this.scheduler = server.getScheduler();
    }

    /**
     * Runs a task asynchronously on the server. The task will be handled 
     * separately from the main server thread.
     *
     * @param task the {@link Runnable} task to be executed asynchronously
     */
    void runAsync(final Runnable task) {
        server.getScheduler().runTask(plugin, task);
    }

    /**
     * Runs a task periodically at a fixed rate asynchronously. The task 
     * will begin after the specified initial delay and continue to run 
     * at the specified period.
     *
     * @param task the {@link Runnable} task to be executed periodically
     * @param initialDelay the delay before the task starts, in server ticks
     * @param period the period between successive executions, in server ticks
     */
    void runTaskTimerAsynchronously(final Runnable task, final long initialDelay, final long period) {
        scheduler.runTaskTimerAsynchronously(plugin, task, initialDelay, period);
    }
}