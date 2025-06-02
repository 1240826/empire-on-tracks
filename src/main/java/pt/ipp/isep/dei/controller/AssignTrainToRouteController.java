package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller used to assign a route to a player's train.
 */
public class AssignTrainToRouteController {

    private final Player player;
    private final List<Station> availableStations;
    private final List<CargoType> availableCargoTypes;
    private List<PointOfRoute> currentRoute = new ArrayList<>();

    public AssignTrainToRouteController(Player player,
                                        List<Station> availableStations,
                                        List<CargoType> availableCargoTypes) {
        this.player = Objects.requireNonNull(player);
        this.availableStations = Objects.requireNonNull(availableStations);
        this.availableCargoTypes = Objects.requireNonNull(availableCargoTypes);
    }

    /** Returns the player's trains. */
    public List<Train> getOwnedTrains() {
        return player.getOwnedTrains();
    }

    public List<Station> getAvailableStations() {
        return availableStations;
    }

    public List<CargoType> getAvailableCargoTypes() {
        return availableCargoTypes;
    }

    /**
     * Gets available cargo types for a specific station.
     * Only cargoes that can be produced/picked up at this station should be returned.
     */
    public List<CargoType> getAvailableCargoTypesForStation(Station station) {
        return station.getCargoes().stream()
                .map(Cargo::getType)
                .distinct()
                .collect(Collectors.toList());
    }

    /** Start a new route in progress. */
    public void startNewRoute() {
        currentRoute.clear();
    }

    /** Add a PointOfRoute (stop) to the current route in progress. */
    public void addPointOfRoute(Station station, List<CargoType> cargoes, CargoMode mode) {
        currentRoute.add(new PointOfRoute(station, cargoes, mode));
    }

    /** Get the current route in progress (immutable). */
    public List<PointOfRoute> getCurrentRoute() {
        return List.copyOf(currentRoute);
    }

    /** Validate and assign the route in progress to the train. */
    public boolean validateAndAssignRoute(Train train) {
        if (currentRoute.size() < 2) return false;
        Route route = new Route(currentRoute);
        train.assignRoute(route);
        return true;
    }
}