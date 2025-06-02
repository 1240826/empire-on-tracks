// SimulatorController.java
package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SimulatorController {
    private final Map map;
    private final Time clock;
    private final ScenarioRepository scenarioRepo;
    private final IndustryRepository industryRepo;
    private final LocomotiveRepository locomotiveRepo;
    private Scenario selectedScenario;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> autoStepFuture;

    /**
     * Constructs a SimulatorController with the required domain components.
     *
     * @param map            The map used for the simulation.
     * @param clock          The simulation clock.
     * @param scenarioRepo   Repository of simulation scenarios.
     * @param industryRepo   Repository of industries.
     * @param locomotiveRepo Repository of locomotives.
     */
    public SimulatorController(Map map,
                               Time clock,
                               ScenarioRepository scenarioRepo,
                               IndustryRepository industryRepo,
                               LocomotiveRepository locomotiveRepo,
    Player player) {
        this.map = map;
        this.clock = clock;
        this.scenarioRepo = scenarioRepo;
        this.industryRepo = industryRepo;
        this.locomotiveRepo = locomotiveRepo;
    }

    /**
     * Starts automatic simulation progression at the given interval in seconds.
     *
     * @param tickIntervalSeconds interval between simulation steps
     */
    public void startSimulation(long tickIntervalSeconds) {
        if (autoStepFuture == null || autoStepFuture.isDone() || autoStepFuture.isCancelled()) {
            autoStepFuture = scheduler.scheduleAtFixedRate(
                    () -> {
                        if (!clock.isFinished()) {
                            stepWithEvents();
                        } else {
                            pauseSimulation();
                        }
                    },
                    0,
                    tickIntervalSeconds,
                    TimeUnit.SECONDS
            );
        }
    }

    /**
     * Pauses the automatic simulation progression.
     */
    public void pauseSimulation() {
        if (autoStepFuture != null) {
            autoStepFuture.cancel(false);
            autoStepFuture = null;
        }
    }

    /**
     * Advances the simulation by one month, updates the map,
     * performs collection/distribution, applies scenarios,
     * and returns a list of events.
     *
     * @return list of event descriptions generated this step
     */
    public List<String> stepWithEvents() {
        clock.advanceByMonth();
        map.update();

        List<String> events = new ArrayList<>();

        // coleta e distribuição
        for (Station st : map.getStations()) {
            events.addAll(st.collectAndDistribute(map.getIndustries()));
        }

        ScenarioRepository repo = Repositories.getInstance().getScenarioRepository();
        repo.addExampleScenario();
        Scenario selected = getSelectedScenario();
        if (selected != null) {
            List<String> scEvents = selected.applyAll(map, map.getLocomotives());
            if (scEvents != null) {
                events.addAll(scEvents);
            }
        }


        // DEBUG: imprimir stocks
        for (Station st : map.getStations()) {
            System.out.println("DEBUG: station " + st.getStationName() +
                    " stock = " + st.getCargoes().stream()
                    .map(c -> c.getType() + ":" + c.getQuantity())
                    .collect(Collectors.joining(", ")));
        }

        return events;
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

    public void setSelectedScenario(Scenario scenario) {
        this.selectedScenario = scenario;
    }

    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    /**
     * Shuts down scheduler and releases resources.
     */
    public void shutdown() {
        pauseSimulation();
        scheduler.shutdownNow();
    }
}
