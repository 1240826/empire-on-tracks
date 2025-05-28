package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Route;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.PointOfRoute;
import pt.ipp.isep.dei.domain.CargoMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller used to assign a route to a player's train.
 */
public class AssignTrainToRouteController {

    private final Player player;
    private final List<Station> availableStations;
    private final List<CargoType> availableCargoTypes;

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
     * This method should be implemented based on station's production capabilities.
     */
    public List<CargoType> getAvailableCargoTypesForStation(Station station) {
        // TODO: Implement based on station's cargo production capabilities
        // For now, return all available cargo types (placeholder implementation)
        return availableCargoTypes;
    }

    /**
     * Builds a Route from user input.
     * New format: "Central, COAL, FULL, Porto, STEEL, HALF, Industrial, FOOD, AVAILABLE"
     * Each Point-of-Route: station, cargo, cargoMode
     */
    public Route parseRouteFromInput(String input) {
        if (input == null || input.isBlank()) return null;
        String[] parts = input.split(",");
        if (parts.length % 3 != 0) return null; // Must be groups of 3

        List<PointOfRoute> routeList = new ArrayList<>();
        for (int i = 0; i < parts.length; i += 3) {
            String stationName = parts[i].trim();
            String cargoName = parts[i + 1].trim();
            String cargoModeName = parts[i + 2].trim();

            Station station = availableStations.stream()
                    .filter(s -> s.getStationName().equalsIgnoreCase(stationName))
                    .findFirst().orElse(null);
            if (station == null) return null;

            CargoType ct = availableCargoTypes.stream()
                    .filter(c -> c.name().equalsIgnoreCase(cargoName))
                    .findFirst().orElse(null);
            if (ct == null) return null;

            CargoMode cargoMode;
            try {
                cargoMode = CargoMode.valueOf(cargoModeName.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null; // Invalid cargo mode
            }

            // Validate cargo is available at this station
            List<CargoType> availableAtStation = getAvailableCargoTypesForStation(station);
            if (!availableAtStation.contains(ct)) {
                return null;
            }

            routeList.add(new PointOfRoute(station, ct, cargoMode));
        }

        try {
            return new Route(routeList);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Assigns the route to the train at the specified index.
     */
    public boolean assignRouteToTrain(int index, Route route) {
        if (route == null) return false;
        List<Train> trains = player.getOwnedTrains();
        if (index < 0 || index >= trains.size()) return false;
        trains.get(index).assignRoute(route);
        return true;
    }
}
