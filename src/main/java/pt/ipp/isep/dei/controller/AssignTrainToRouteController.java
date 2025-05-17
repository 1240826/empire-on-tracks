package pt.ipp.isep.dei.controller;

import java.util.ArrayList;
import java.util.List;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.Route;

public class AssignTrainToRouteController {
    private final Player player;
    private final List<Station> availableStations;
    private final List<CargoType> availableCargoTypes;

    public AssignTrainToRouteController(Player player, List<Station> availableStations, List<CargoType> availableCargoTypes) {
        this.player = player;
        this.availableStations = availableStations;
        this.availableCargoTypes = availableCargoTypes;
    }

    public List<Train> getOwnedTrains() {
        return player.getOwnedTrains();
    }

    public List<Station> getAvailableStations() {
        return availableStations;
    }

    public List<CargoType> getAvailableCargoTypes() {
        return availableCargoTypes;
    }

    // --- Inner static helper class for pairing ---
    public static class StationCargo {
        private final Station station;
        private final CargoType cargoType;

        public StationCargo(Station station, CargoType cargoType) {
            this.station = station;
            this.cargoType = cargoType;
        }

        public Station getStation() { return station; }
        public CargoType getCargoType() { return cargoType; }

        @Override
        public String toString() {
            return station.getStationName() + " (" + cargoType.name() + ")";
        }
    }

    /**
     * Parses the assignment string and builds a Route.
     * @param input Assignment string (e.g. "Station A, Steel, Station B, Iron, Station B, Steel, Station C, Iron")
     * @return Route (with parsed StationCargo), or null if parsing fails
     */
    public Route parseRouteFromInput(String input) {
        String[] parts = input.split(",");
        if (parts.length % 2 != 0) return null; // must be pairs

        List<StationCargo> routeList = new ArrayList<>();
        for (int i = 0; i < parts.length; i += 2) {
            String stationName = parts[i].trim();
            String cargoTypeName = parts[i + 1].trim();

            Station station = availableStations.stream()
                    .filter(s -> s.getStationName().equalsIgnoreCase(stationName))
                    .findFirst().orElse(null);
            if (station == null) return null;

            CargoType cargoType = availableCargoTypes.stream()
                    .filter(c -> c.name().equalsIgnoreCase(cargoTypeName))
                    .findFirst().orElse(null);
            if (cargoType == null) return null;

            routeList.add(new StationCargo(station, cargoType));
        }
        return new Route(routeList); // Route will use List<AssignTrainToRouteController.StationCargo>
    }

    /**
     * Assigns a route to the train (by index in the player's train list).
     * @return true if successful
     */
    public boolean assignRouteToTrain(int trainIndex, Route route) {
        List<Train> trains = player.getOwnedTrains();
        if (trainIndex < 0 || trainIndex >= trains.size()) return false;

        Train train = trains.get(trainIndex);
        train.assignRoute(route); // Overwrites previous route
        return true;
    }
}
