// SimulationClock.java
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
     * Constructs the simulation clock.
     *
     * @param startDate           Initial date of the simulation.
     * @param tickIntervalSeconds Interval in seconds for each automatic month tick.
     */
    public Date(LocalDateTime startDate, long tickIntervalSeconds) {
        this.currentDate          = startDate;
        this.tickIntervalSeconds  = tickIntervalSeconds;
    }

    /**
     * Returns the current date of the simulator.
     *
     * @return the current simulation date
     */
    public LocalDateTime getDate() {
        return currentDate;
    }

    /**
     * Starts auto-tick (advances a month every tickIntervalSeconds).
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
     * Pauses the auto-tick.
     */
    public void stop() {
        if (futureTick != null) {
            futureTick.cancel(false);
            futureTick = null;
        }
    }

    /**
     * Advances the clock by one month manually.
     */
    public void advanceManually() {
        currentDate = currentDate.plusMonths(1);
    }

    /**
     * Shuts down the scheduler (when closing the application).
     */
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
