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
            String cargoNames = parts[i + 1].trim();
            String cargoModeName = parts[i + 2].trim();

            Station station = availableStations.stream()
                    .filter(s -> s.getStationName().equalsIgnoreCase(stationName))
                    .findFirst().orElse(null);
            if (station == null) return null;

            String[] cargoArray = cargoNames.split("\\+");
            List<CargoType> cargoes = new ArrayList<>();
            for (String cargoStr : cargoArray) {
                CargoType ct = availableCargoTypes.stream()
                        .filter(c -> c.name().equalsIgnoreCase(cargoStr.trim()))
                        .findFirst().orElse(null);
                if (ct == null) return null; // invalid cargo
                // Validate cargo is available at this station
                List<CargoType> availableAtStation = getAvailableCargoTypesForStation(station);
                if (!availableAtStation.contains(ct)) {
                    return null; // cargo not available at this station
                }
                cargoes.add(ct);
            }
            if (cargoes.isEmpty()) return null;

            CargoMode cargoMode;
            try {
                cargoMode = CargoMode.valueOf(cargoModeName.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null; // Invalid cargo mode
            }

            routeList.add(new PointOfRoute(station, cargoes, cargoMode));
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
