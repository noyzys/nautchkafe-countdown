package dev.nautchkafe.countdown;

import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A utility class that maps countdown durations to corresponding actions.
 * The countdown action is triggered based on the remaining seconds.
 *
 * @param <TYPE> The type of the object passed to the consumer when the countdown reaches a specific duration.
 */
final class CountdownAlertMapper<TYPE> {

    private final Map<Long, Consumer<TYPE>> secondsMap;
    private static final Consumer<Object> NO_OP = empty -> {};

    /**
     * Private constructor for creating an instance of {@code CountdownAlertMapper}.
     *
     * @param secondsMap A map where keys are seconds (long) and values are consumers handling the given type.
     */
    private CountdownAlertMapper(final Map<Long, Consumer<TYPE>> secondsMap) {
        this.secondsMap = secondsMap;
    }

    /**
     * Creates a {@code CountdownAlertMapper} from a map of {@code Duration} to corresponding actions.
     *
     * @param input A map where keys are durations and values are consumer actions to be executed when
     *              the specified duration is reached.
     * @param <TYPE> The type of the object being consumed.
     * @return A new instance of {@code CountdownAlertMapper}.
     */
    public static <TYPE> CountdownAlertMapper<TYPE> fromDurations(final Map<Duration, Consumer<TYPE>> input) {
        return new CountdownAlertMapper<>(input.entrySet()
            .stream()
            .collect(Collectors.toMap(
                e -> e.getKey().getSeconds(),
                Map.Entry::getValue
            )));
    }

    /**
     * Resolves the appropriate consumer based on the given seconds remaining.
     * 
     * @param secondsLeft The amount of seconds left.
     * @return The corresponding consumer action, or a no-operation action if no match is found.
     */
    Consumer<TYPE> resolve(final long secondsLeft) {
        return secondsMap.getOrDefault(secondsLeft, NO_OP);
    }

    /**
     * Converts this mapper to a countdown ticker capable of executing countdown actions.
     *
     * @return A {@code CountdownTicker} that triggers actions based on the remaining seconds.
     */
    CountdownTicker toCountdownTicker() {
        return (countdownId, sec, ticks) -> resolve(sec).accept(countdownId);
    }
}
