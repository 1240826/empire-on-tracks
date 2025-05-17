// SimulationClock.java
package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.ScenarioRepository;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Mantém a data da simulação, iniciando-a com o valor
 * definido em ScenarioRepository e avançando 1 mês a cada 20 segundos.
 */
public class Date {

    private LocalDateTime currentDate;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    /**
     * Constroi o clock buscando a data inicial no repositório de cenários.
     * @param scenarioRepo repositório de cenários contendo a data inicial
     */
    public Date(ScenarioRepository scenarioRepo) {
        // Assumimos que o cenário inicial está mapeado para uma chave de data "start"
        // ou que o repositório tem um método específico para data inicial.
        LocalDateTime initial = scenarioRepo.getInitialDate();
        this.currentDate = (initial != null) ? initial : LocalDateTime.now();
    }

    /**
     * Inicia o agendamento: a cada 20 segundos acrescenta 1 mês.
     */
    public void start() {
        // delay inicial de 20s, e então periodicamente a cada 20s
        scheduler.scheduleAtFixedRate(() -> {
            currentDate = currentDate.plusMonths(1);
        }, 20, 20, TimeUnit.SECONDS);
    }

    /**
     * Para o agendamento (desliga o scheduler).
     */
    public void stop() {
        scheduler.shutdownNow();
    }

    /**
     * Retorna a data corrente da simulação.
     */
    public LocalDateTime getDate() {
        return currentDate;
    }
}
