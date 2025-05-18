package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Scenario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScenarioRepository {
    private LocalDateTime initialDate;
    private final List<Scenario> scenarios = new ArrayList<>();

    public void addScenario(LocalDateTime march, Scenario scenario) {
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

    /**
     * Define o cenário inicial: armazena a data e o próprio cenário na lista.
     */
    public void addInitialScenario(LocalDateTime startDate, Scenario initial) {
        // guarda a data de início
        this.initialDate = startDate;
        // opcional: sincroniza a triggerDate do cenário, caso exista esse setter
        initial.setTriggerDate(startDate);
        // adiciona à lista interna
        addScenario(initial);
    }
}
