package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.ArrayList;
import java.util.List;

public class LocomotiveCatalog {
    private List<Locomotive> allLocomotives = new ArrayList<>();

    public void addLocomotive(Locomotive loco) {
        allLocomotives.add(loco);
    }

    public List<Locomotive> getAvailableLocomotives() {
        // In future: filter based on scenario
        return LocomotiveRepository.getInstance().getAllTemplates();
    }
}

