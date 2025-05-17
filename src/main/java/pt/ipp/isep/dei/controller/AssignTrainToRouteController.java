// File: pt/ipp/isep/dei/controller/AssignTrainToRouteController.java
package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller used to assign a Route to a Player's Train.
 */
public class AssignTrainToRouteController {

    private final Player player;
    private final List<Station> availableStations;
    private final List<CargoType> availableCargoTypes;

    /**
     * @param player               the player owning the trains
     * @param availableStations    stations the player may use in routes
     * @param availableCargoTypes  cargo types the player may load
     */
    public AssignTrainToRouteController(Player player,
                                        List<Station> availableStations,
                                        List<CargoType> availableCargoTypes) {
        this.player               = Objects.requireNonNull(player);
        this.availableStations    = Objects.requireNonNull(availableStations);
        this.availableCargoTypes  = Objects.requireNonNull(availableCargoTypes);
    }

    /**
     * Returns the list of Trains owned by the player.
     */
    public List<Train> getOwnedTrains() {
        return player.getOwnedTrains();
    }

    /**
     * Returns the list of Stations that can be used in route definitions.
     */
    public List<Station> getAvailableStations() {
        return availableStations;
    }

    /**
     * Returns the list of CargoTypes that can be loaded.
     */
    public List<CargoType> getAvailableCargoTypes() {
        return availableCargoTypes;
    }

    // --- Inner helper class for pairing a Station with a CargoType --- //

    /**
     * Represents a stop on the route: a Station plus the CargoType to load there.
     */
    public static class StationCargo {
        private final Station station;
        private final CargoType cargoType;

        public StationCargo(Station station, CargoType cargoType) {
            this.station   = Objects.requireNonNull(station);
            this.cargoType = Objects.requireNonNull(cargoType);
        }

        public Station getStation() {
            return station;
        }

        public CargoType getCargoType() {
            return cargoType;
        }

        @Override
        public String toString() {
            return station.getStationName() + " (" + cargoType + ")";
        }
    }

    /**
     * Parses a user-input string into a Route.
     * Example input: "Central, COAL, Porto, STEEL, Industrial, FOOD"
     *
     * @param input a comma-separated list of station name and cargo type pairs
     * @return a new Route if parsing succeeds; null on any error
     */
    public Route parseRouteFromInput(String input) {
        if (input == null || input.isBlank()) {
            return null;
        }
        String[] parts = input.split(",");
        if (parts.length % 2 != 0) {
            return null; // must come in pairs
        }

        List<StationCargo> routeList = new ArrayList<>();
        for (int i = 0; i < parts.length; i += 2) {
            String stationName   = parts[i].trim();
            String cargoTypeName = parts[i + 1].trim();

            Station station = availableStations.stream()
                    .filter(s -> s.getStationName().equalsIgnoreCase(stationName))
                    .findFirst().orElse(null);
            if (station == null) {
                return null;
            }

            CargoType cargoType = availableCargoTypes.stream()
                    .filter(c -> c.name().equalsIgnoreCase(cargoTypeName))
                    .findFirst().orElse(null);
            if (cargoType == null) {
                return null;
            }

            routeList.add(new StationCargo(station, cargoType));
        }

        return new Route(routeList);
    }

    /**
     * Assigns the given Route to the train at the specified index in the player's list.
     *
     * @param trainIndex index in the player's owned trains list
     * @param route      the Route to assign
     * @return true if assignment succeeded; false if index invalid or route null
     */
    public boolean assignRouteToTrain(int trainIndex, Route route) {
        if (route == null) {
            return false;
        }
        List<Train> trains = player.getOwnedTrains();
        if (trainIndex < 0 || trainIndex >= trains.size()) {
            return false;
        }

        Train train = trains.get(trainIndex);
        train.assignRoute(route);
        return true;
    }
}
