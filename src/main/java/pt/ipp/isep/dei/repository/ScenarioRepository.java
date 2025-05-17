// ScenarioRepository.java
package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Scenario;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ScenarioRepository {
    private final Map<LocalDateTime, Scenario> scenarios = new HashMap<>();
    private LocalDateTime initialDate;

    /**
     * Define o cenário que será considerado inicial e sua data.
     */
    public void addInitialScenario(LocalDateTime date, Scenario scenario) {
        this.initialDate = date;
        scenarios.put(date, scenario);
    }

    /**
     * Recupera a data inicial definida.
     */
    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    /**
     * Retorna o cenário associado a uma data específica.
     */
    public Scenario changeScenario(LocalDateTime date) {
        return scenarios.get(date);
    }
}
