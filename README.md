                                                # bukkit-countdown 


**A flexible and efficient countdown system built for Bukkit/Spigot servers.**

- This system allows for customizable, multi-phase countdowns, supporting events such as start, finish, cancel, and close with full event handling and cancellation support.

## Features:
- Multi-phase countdowns: Support for different phases with individual durations.
- Event handling: Triggers events like CountdownStartEvent, CountdownFinishEvent, CountdownCancelEvent, and CountdownCloseEvent.
- Asynchronous Execution: Countdown execution is handled asynchronously to prevent blocking the main server thread.
- Event-driven architecture: Triggers specific events during the countdown lifecycle to allow for better interaction and flexibility.

## Example use case:

```java
CountdownTimer countdown = new CountdownTimer(server);

Duration tenSecond = Duration.ofSeconds(10);
Duration fiveSecond = Duration.ofSeconds(5);
CountdownAlertMapper<String> alerts = CountdownAlertMapper.fromDurations(Map.of(
        tenSecond, id -> server.broadcastMessage("> 10 seconds to start: " + id),
        fiveSecond, id -> server.broadcastMessage("> 5 seconds to start: " + id)
                                      ));

CountdownPhase prepare = new CountdownPhase(Duration.ofSeconds(15), alerts.toCountdownTicker());
CountdownPhase active = new CountdownPhase(Duration.ofSeconds(10), (id, sec, tick) -> server.broadcastMessage("[" + id + "] Game ongoing... Seconds left: " + sec));

countdown.startPhasedCountdown(
    "countdown-id",
    List.of(prepare, active),
    (id, end) -> server.broadcastMessage("> Countdown finished for " + id),
    id -> server.broadcastMessage("> Countdown cancelled for: " + id)
);

```
## Event driven stack:
- **CountdownStartEvent** - Triggered when the countdown starts.
- **CountdownFinishEvent** - Triggered when the countdown finishes successfully all phases have completed).
- **CountdownCancelEvent** - Triggered when the countdown is canceled before completion.
- **CountdownCloseEvent** - Triggered when the countdown is closed, either after completion or cancellation.

```java
@EventHandler
public void onCountdownStart(CountdownStartEvent event) {
    String countdownId = event.getCountdownId();
    getServer().broadcastMessage("Countdown started for: " + countdownId);
}

@EventHandler
public void onCountdownFinish(CountdownFinishEvent event) {
    String countdownId = event.getCountdownId();
    getServer().broadcastMessage("Countdown finished for: " + countdownId);
}

@EventHandler
public void onCountdownCancel(CountdownCancelEvent event) {
    String countdownId = event.getCountdownId();
    getServer().broadcastMessage("Countdown canceled for: " + countdownId);
}

@EventHandler
public void onCountdownClose(CountdownCloseEvent event) {
    String countdownId = event.getCountdownId();
    getServer().broadcastMessage("Countdown closed for: " + countdownId);
}
```



**If you are interested in exploring functional programming and its applications within this project visit the repository at [vavr-in-action](https://github.com/noyzys/bukkit-vavr-in-action), [fp-practice](https://github.com/noyzys/fp-practice).**
