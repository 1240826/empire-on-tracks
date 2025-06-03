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

/**
 * Controller responsible for running the simulation, handling time progression,
 * industry and station updates, and moving trains along their routes.
 */
public class SimulatorController {
    private final Map map;
    private final Time clock;
    private final ScenarioRepository scenarioRepo;
    private final IndustryRepository industryRepo;
    private final LocomotiveRepository locomotiveRepo;
    private final Player player;
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
     * @param player         The player whose trains will be simulated.
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
        this.player = player;
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
     * performs industry and station updates, moves all player trains,
     * applies scenario events, and returns a list of generated events.
     *
     * @return list of event descriptions generated this step
     */
    public List<String> stepWithEvents() {
        clock.advanceByMonth();
        map.update();

        List<String> events = new ArrayList<>();

        // --- Move player trains along their assigned routes ---
        for (Train train : player.getOwnedTrains()) {
            // (a) If currently traveling, advance by one month
            if (train.isInTransit()) {
                boolean justArrived = train.advanceTravelByOneMonth();
                if (justArrived) {
                    Station arrived = train.getAssignedRoute()
                            .getPointsOfRoute()
                            .get(train.getCurrentStopIndex())
                            .getStation();
                    // Pick up cargo assigned for this stop
                    for (CargoType ctype : train.getAssignedCargoTypesForCurrentStop()) {
                        // (Assuming you have a method like collectCargoOfType on Station)
                        int collectedQty = arrived.collectCargoOfType(ctype, train); // You may need to implement this!
                        if (collectedQty > 0) {
                            events.add("Train '" + train.getName() + "' collected "
                                    + collectedQty + " of " + ctype
                                    + " at station '" + arrived.getStationName() + "'");
                        }
                    }
                    events.add("Train '" + train.getName() + "' arrived at station '" +
                            arrived.getStationName() + "'");
                }
            }
            // (b) If at station and has a route, start traveling to the next stop
            else if (train.hasRoute()) {
                Station nextStation = train.getNextStationOnRoute();
                if (nextStation != null) {
                    Station currentStation = train.getAssignedRoute()
                            .getPointsOfRoute()
                            .get(train.getCurrentStopIndex())
                            .getStation();
                    double distanceKm = map.getShortestDistance(currentStation, nextStation);
                    if (Double.isFinite(distanceKm)) {
                        train.startTravelTo(nextStation, distanceKm);
                        events.add(
                                "Train '" + train.getName() + "' departed " +
                                        currentStation.getStationName() +
                                        " for " + nextStation.getStationName() +
                                        " (distance: " + String.format("%.1f", distanceKm) + " km)"
                        );
                    } else {
                        events.add(
                                "ERROR: No rail route from '" + currentStation.getStationName() +
                                        "' to '" + nextStation.getStationName() + "' for train '" + train.getName() + "'"
                        );
                    }
                }
            }
        }

        // --- Industry to station collection/distribution (optional, for your legacy logic) ---
        for (Station st : map.getStations()) {
            events.addAll(st.collectAndDistribute(map.getIndustries()));
        }

        // --- Scenario events (if applicable) ---
        ScenarioRepository repo = Repositories.getInstance().getScenarioRepository();
        Scenario selected = getSelectedScenario();
        if (selected != null) {
            List<String> scEvents = selected.applyAll(map, map.getLocomotives());
            if (scEvents != null) {
                events.addAll(scEvents);
            }
        }

        // --- Debug print: station stocks ---
        for (Station st : map.getStations()) {
            System.out.println("DEBUG: station " + st.getStationName() +
                    " stock = " + st.getCargoes().stream()
                    .map(c -> c.getType() + ":" + c.getQuantity())
                    .collect(Collectors.joining(", ")));
        }

        return events;
    }

    /**
     * Returns the current simulation date/time.
     *
     * @return current date in simulation
     */
    public LocalDateTime getCurrentDate() {
        return clock.getDate();
    }

    /**
     * Returns the industries present on the map.
     *
     * @return list of industries
     */
    public List<Industry> getIndustries() {
        return map.getIndustries();
    }

    /**
     * Returns the stations present on the map.
     *
     * @return list of stations
     */
    public List<Station> getStations() {
        return map.getStations();
    }

    /**
     * Returns the locomotives present on the map.
     *
     * @return list of locomotives
     */
    public List<Locomotive> getMapLocomotives() {
        return map.getLocomotives();
    }

    /**
     * Returns the available locomotive templates from the repository.
     *
     * @return list of locomotive templates
     */
    public List<Locomotive> getLocomotiveTemplates() {
        return locomotiveRepo.getAllTemplates();
    }

    /**
     * Sets the currently selected scenario.
     *
     * @param scenario the scenario to select
     */
    public void setSelectedScenario(Scenario scenario) {
        this.selectedScenario = scenario;
    }

    /**
     * Returns the currently selected scenario.
     *
     * @return the selected scenario
     */
    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    /**
     * Shuts down the scheduler and releases resources.
     */
    public void shutdown() {
        pauseSimulation();
        scheduler.shutdownNow();
    }
}
