package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Event that removes an existing locomotive from the map by index.
 */
public class RemoveTemplateLocomotiveEvent implements ScenarioEvent {
    private final int locomotiveIndex;

    /**
     * Constructs a RemoveTemplateLocomotiveEvent for the specified index.
     *
     * @param locomotiveIndex the index of the locomotive to remove
     */
    public RemoveTemplateLocomotiveEvent(int locomotiveIndex) {
        this.locomotiveIndex = locomotiveIndex;
    }

    /**
     * Applies the event by removing the locomotive at the given index from the list of locomotives.
     *
     * @param map         the map (unused in this event)
     * @param locomotives the list of locomotives from which to remove
     */
    @Override
    public void apply(Map map, List<Locomotive> locomotives) {
        if (locomotiveIndex >= 0 && locomotiveIndex < locomotives.size()) {
            locomotives.remove(locomotiveIndex);
        }
    }
}
