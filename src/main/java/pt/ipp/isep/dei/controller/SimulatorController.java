// SimulatorController.java
package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller que une mapa, relógio e cenários.
 */
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
        this.map             = map;
        this.clock           = clock;
        this.scenarioRepo    = scenarioRepo;
        this.industryRepo    = industryRepo;
        this.locomotiveRepo  = locomotiveRepo;
    }

    /** Liga o auto-tick do clock. */
    public void startSimulation() {
        clock.start();
    }

    /** Pausa o auto-tick do clock. */
    public void pauseSimulation() {
        clock.stop();
    }

    /** Um passo manual: +1 mês, atualiza mapa e dispara cenários. */
    public void step() {
        clock.advanceManually();
        map.update();

        LocalDateTime now = clock.getDate();
        List<Scenario> todays = scenarioRepo.getScenariosForDate(now);
        for (Scenario sc : todays) {
            sc.applyAll(map, map.getLocomotives());
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

    public List<Locomotive> getMapLocomotives() {
        return map.getLocomotives();
    }

    public List<Locomotive> getLocomotiveTemplates() {
        return locomotiveRepo.getAllTemplates();
    }

    public void shutdown() {
        clock.shutdown();
    }
}
