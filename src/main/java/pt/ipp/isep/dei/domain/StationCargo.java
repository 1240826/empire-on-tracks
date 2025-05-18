package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class StationCargo {
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
