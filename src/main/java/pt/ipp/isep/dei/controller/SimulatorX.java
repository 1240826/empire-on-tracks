package pt.ipp.isep.dei.controller;// SimulatorController.java


import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.List;

public class SimulatorController {
    private final Map map;
    private final Date clock;
    private final ScenarioRepository scenarioRepo;
    private final IndustryRepository industryRepo;
    private final LocomotiveRepository locomotiveRepo;

    public SimulatorController(Map map,
                               Date clock,
                               ScenarioRepository scenarioRepo,
                               IndustryRepository industryRepo,
                               LocomotiveRepository locomotiveRepo) {
        this.map           = map;
        this.clock         = clock;
        this.scenarioRepo  = scenarioRepo;
        this.industryRepo  = industryRepo;
        this.locomotiveRepo= locomotiveRepo;
    }

    public void startSimulation() {
        clock.start();
    }

    public void pauseSimulation() {
        clock.stop();
    }

    public void step() {
        // 1) Avança um tick da simulação
        map.update();

        // 2) Dispara quaisquer cenários para a data atual
        LocalDateTime now = clock.getDate();
        List<Scenario> todays = scenarioRepo.getScenariosForDate(now);
        for (Scenario sc : todays) {
            sc.applyAll(industryRepo, locomotiveRepo);
        }
    }

    public LocalDateTime getCurrentDate() {
        return clock.getDate();
    }

    public List<Industry> getIndustries() {
        return map.getIndustries();
    }

    public List<Station> getStations() {
        return map.getStations();
    }

    public List<Locomotive> getLocomotiveTemplates() {
        return locomotiveRepo.getAllTemplates();
    }
}
