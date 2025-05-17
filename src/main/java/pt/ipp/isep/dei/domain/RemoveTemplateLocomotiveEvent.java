package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Evento que remove uma locomotiva existente no mapa pelo índice.
 */
public class RemoveTemplateLocomotiveEvent implements ScenarioEvent {
    private final int locomotiveIndex;

    public RemoveTemplateLocomotiveEvent(int locomotiveIndex) {
        this.locomotiveIndex = locomotiveIndex;
    }

    @Override
    public void apply(Map map, List<Locomotive> locomotives) {
        if (locomotiveIndex >= 0 && locomotiveIndex < locomotives.size()) {
            locomotives.remove(locomotiveIndex);
        }
    }
}
