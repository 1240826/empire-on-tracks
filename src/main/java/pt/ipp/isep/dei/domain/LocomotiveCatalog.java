package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a catalog of all locomotives available in the system.
 * Responsible for storing and providing access to locomotive data.
 */
public class LocomotiveCatalog {
    private List<Locomotive> allLocomotives = new ArrayList<>();

    /**
     * Adds a new locomotive to the catalog.
     *
     * @param loco the locomotive to be added
     */
    public void addLocomotive(Locomotive loco) {
        allLocomotives.add(loco);
    }

    /**
     * Retrieves a list of all locomotives currently available in the catalog.
     * In future implementations, this method may filter based on scenario or other constraints.
     *
     * @return a new list containing all available locomotives
     */
    public List<Locomotive> getAvailableLocomotives() {
        return new ArrayList<>(allLocomotives);
    }
}
