package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

public class LocomotiveCatalog {
    private List<Locomotive> allLocomotives = new ArrayList<>();

    public void addLocomotive(Locomotive loco) {
        allLocomotives.add(loco);
    }

    public List<Locomotive> getAvailableLocomotives() {
        // In future: filter based on scenario
        return new ArrayList<>(allLocomotives);
    }
}

