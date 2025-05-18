// SimulatorController.java
package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller that links together the map, simulation clock, and scenario management,
 * coordinating simulation updates and scenario application during the simulation lifecycle.
 */
public class SimulatorController {
    private final Map map;
    private final Date clock;
    private final ScenarioRepository scenarioRepo;
    private final IndustryRepository industryRepo;
    private final LocomotiveRepository locomotiveRepo;

    /**
     * Constructs a SimulatorController with the required domain components.
     *
     * @param map              The map used for the simulation.
     * @param clock            The simulation clock.
     * @param scenarioRepo     Repository of simulation scenarios.
     * @param industryRepo     Repository of industries.
     * @param locomotiveRepo   Repository of locomotives.
     */
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

    /** Starts the auto-tick of the clock (automatic simulation progression). */
    public void startSimulation() {
        clock.start();
    }

    /** Pauses the auto-tick of the clock (pauses simulation progression). */
    public void pauseSimulation() {
        clock.stop();
    }

    /**
     * Performs a manual step: advances the simulation by one month,
     * updates the map, and triggers scenarios for the current date.
     */
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


    // SimulatorController.java
    public List<String> stepWithEvents() {
        clock.advanceManually();

        // 1) produção pura
        map.update();

        List<String> events = new ArrayList<>();

        // 2) coleta+distribuição (tudo num só método)
        for (Station st : map.getStations()) {
            events.addAll(st.collectAndDistribute(map.getIndustries()));
        }

        // 3) cenários
        LocalDateTime now = clock.getDate();
        for (Scenario sc : scenarioRepo.getScenariosForDate(now)) {
            List<String> scE = sc.applyAll(map, map.getLocomotives());
            if (scE != null) events.addAll(scE);
        }

        // DEBUG: imprime stock real das estações
        for (Station st : map.getStations()) {
            System.out.println("DEBUG pós-step: estação " + st.getStationName() +
                    " stock = " + st.getCargoes().stream()
                    .map(c -> c.getType() + ":" + c.getQuantity())
                    .collect(Collectors.joining(", ")));
        }

        return events;
    }


}