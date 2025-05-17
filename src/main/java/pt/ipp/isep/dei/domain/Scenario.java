package pt.ipp.isep.dei.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Um cenário reúne uma data de disparo e uma lista de eventos
 * que atuam diretamente no mapa e nas locomotivas.
 */
public class Scenario {
    private final LocalDateTime triggerDate;
    private final List<ScenarioEvent> events = new ArrayList<>();

    public Scenario(LocalDateTime triggerDate) {
        this.triggerDate = triggerDate;
    }

    public LocalDateTime getTriggerDate() {
        return triggerDate;
    }

    public void addEvent(ScenarioEvent event) {
        events.add(event);
    }

    /**
     * Aplica todos os eventos sobre o mapa e a lista de trens.
     */
    public void applyAll(Map map, List<Locomotive> locomotives) {
        for (ScenarioEvent ev : events) {
            ev.apply(map, locomotives);
        }
    }
}
