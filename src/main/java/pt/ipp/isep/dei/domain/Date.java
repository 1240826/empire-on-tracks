package pt.ipp.isep.dei.domain;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * Simulation clock with configurable auto-tick and manual tick support.
 */
public class Date {

    private LocalDateTime currentDate;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> futureTick;
    private final long tickIntervalSeconds;

    /**
     * Constructs a simulation clock with a starting date and tick interval.
     *
     * @param startDate           the initial simulation date
     * @param tickIntervalSeconds the interval (in seconds) between automatic monthly ticks
     */
    public Date(LocalDateTime startDate, long tickIntervalSeconds) {
        this.currentDate = startDate;
        this.tickIntervalSeconds = tickIntervalSeconds;
    }

    /**
     * Returns the current date of the simulation.
     *
     * @return the current simulation date
     */
    public LocalDateTime getDate() {
        return currentDate;
    }

    /**
     * Starts the automatic ticking mechanism (advances by one month every tickIntervalSeconds).
     */
    public void start() {
        if (futureTick == null || futureTick.isDone() || futureTick.isCancelled()) {
            futureTick = scheduler.scheduleAtFixedRate(
                    () -> currentDate = currentDate.plusMonths(1),
                    tickIntervalSeconds,
                    tickIntervalSeconds,
                    TimeUnit.SECONDS
            );
        }
    }

    /**
     * Stops the automatic ticking.
     */
    public void stop() {
        if (futureTick != null) {
            futureTick.cancel(false);
            futureTick = null;
        }
    }

    /**
     * Advances the simulation clock manually by one month.
     */
    public void advanceManually() {
        currentDate = currentDate.plusMonths(1);
    }

    /**
     * Shuts down the internal executor. Should be called when the application exits.
     */
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
