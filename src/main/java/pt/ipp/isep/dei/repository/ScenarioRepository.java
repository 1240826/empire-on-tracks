package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Scenario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia todos os cenários agendados.
 */
public class ScenarioRepository {
    private LocalDateTime initialDate;


    private final List<Scenario> scenarios = new ArrayList<>();

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
    }

    /**
     * Retorna todos os cenários cuja data de disparo seja exatamente `date`.
     */
    public List<Scenario> getScenariosForDate(LocalDateTime date) {
        List<Scenario> result = new ArrayList<>();
        for (Scenario sc : scenarios) {
            if (sc.getTriggerDate().equals(date)) {
                result.add(sc);
            }
        }
        return result;
    }
    public void setInitialDate(LocalDateTime date) {
        this.initialDate = date;
    }

    public LocalDateTime getInitialDate() {
        return initialDate;
    }

    public void addInitialScenario(LocalDateTime startDate, Scenario initial) {

    }
}
