package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.Objects;

/**
 * Scenario event that adds a new locomotive to the map as a template instance.
 * <p>
 * The locomotive is initially created with an empty route.
 */
public class AddTemplateLocomotiveEvent implements ScenarioEvent {
    private final Locomotive template;

    /**
     * Constructs an event that will add a locomotive based on the given template.
     *
     * @param template the locomotive template to clone and add to the map
     * @throws NullPointerException if the template is null
     */
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
