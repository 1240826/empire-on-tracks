// SimulationClock.java
package pt.ipp.isep.dei.domain;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * Relógio da simulação com auto-tick configurável e tick manual.
 */
public class Date {
    private LocalDateTime currentDate;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> futureTick;
    private final long tickIntervalSeconds;

    /** @param startDate Data inicial da simulação */
    /** @param tickIntervalSeconds Intervalo em segundos para cada mês automático */
    public Date(LocalDateTime startDate, long tickIntervalSeconds) {
        this.currentDate          = startDate;
        this.tickIntervalSeconds  = tickIntervalSeconds;
    }



    /** Data atual do simulador. */
    public LocalDateTime getDate() {
        return currentDate;
    }

    /** Inicia auto-tick (mês a cada tickIntervalSeconds). */
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

    /** Pausa auto-tick. */
    public void stop() {
        if (futureTick != null) {
            futureTick.cancel(false);
            futureTick = null;
        }
    }

    /** Avança manualmente um mês. */
    public void advanceManually() {
        currentDate = currentDate.plusMonths(1);
    }

    /** Desliga o executor (ao encerrar a app). */
    public void shutdown() {
        scheduler.shutdownNow();
    }
}
