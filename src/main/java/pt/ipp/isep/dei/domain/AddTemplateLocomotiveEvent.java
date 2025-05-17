package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.Objects;

/**
 * Evento que adiciona uma nova locomotiva ao mapa (rota vazia inicialmente).
 */
public class AddTemplateLocomotiveEvent implements ScenarioEvent {
    private final Locomotive template;

    public AddTemplateLocomotiveEvent(Locomotive template) {
        this.template = Objects.requireNonNull(template);
    }

    @Override
    public void apply(Map map, List<Locomotive> locomotives) {
        // Clona o template e coloca no mapa
        Locomotive clone = new Locomotive(
                template.getId(),
                template.getType(),
                template.getPrice(),
                template.requiresElectrification(),
                template.getPower(),
                template.getAcceleration(),
                template.getTopSpeed(),
                template.getStartYear(),
                template.getFuelCostPerYear(),
                template.getMaintenancePerYear(),
                template.getCapacity(),
                List.of()  // rota inicial vazia
        );
        locomotives.add(clone);
    }
}
