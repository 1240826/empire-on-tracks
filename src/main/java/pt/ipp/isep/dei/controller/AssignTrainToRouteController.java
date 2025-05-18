// File: pt/ipp/isep/dei/controller/AssignTrainToRouteController.java
package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Route;
import pt.ipp.isep.dei.domain.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Controller usado para atribuir uma rota a um trem do jogador.
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

    /** Retorna os trens do jogador. */
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
     * Representa uma parada: estação + tipo de carga.
     */
    public static class StationCargo {
        private final Station station;
        private final CargoType cargoType;

        public StationCargo(Station station, CargoType cargoType) {
            this.station = Objects.requireNonNull(station);
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
     * Constrói uma Route a partir de input do utilizador:
     * ex: "Central, COAL, Porto, STEEL, Industrial, FOOD"
     */
    public Route parseRouteFromInput(String input) {
        if (input == null || input.isBlank()) return null;
        String[] parts = input.split(",");
        if (parts.length % 2 != 0) return null;

        List<StationCargo> routeList = new ArrayList<>();
        for (int i = 0; i < parts.length; i += 2) {
            String stationName = parts[i].trim();
            String cargoName   = parts[i + 1].trim();

            Station station = availableStations.stream()
                    .filter(s -> s.getStationName().equalsIgnoreCase(stationName))
                    .findFirst().orElse(null);
            if (station == null) return null;

            CargoType ct = availableCargoTypes.stream()
                    .filter(c -> c.name().equalsIgnoreCase(cargoName))
                    .findFirst().orElse(null);
            if (ct == null) return null;

            routeList.add(new StationCargo(station, ct));
        }

        try {
            return new Route(routeList);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Atribui a rota ao trem no índice especificado.
     */
    public boolean assignRouteToTrain(int index, Route route) {
        if (route == null) return false;
        List<Train> trains = player.getOwnedTrains();
        if (index < 0 || index >= trains.size()) return false;
        trains.get(index).assignRoute(route);
        return true;
    }
}
