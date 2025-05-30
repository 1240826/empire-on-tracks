package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Represents a Point-of-Route: a trio of station, single cargo, and cargo mode.
 * Each Point-of-Route corresponds to picking up one cargo type at a station,
 * using a specific cargo loading mode.
 */
public class PointOfRoute {
    private final Station station;
    private final CargoType cargo;
    private final CargoMode cargoMode;

    /**
     * Constructs a Point-of-Route.
     * @param station the station where the train stops
     * @param cargo the cargo type to be picked up at this station
     * @param cargoMode the loading mode (FULL, HALF, or AVAILABLE)
     * @throws IllegalArgumentException if any argument is null
     */
    public PointOfRoute(Station station, CargoType cargo, CargoMode cargoMode) {
        this.station = Objects.requireNonNull(station, "Station cannot be null");
        this.cargo = Objects.requireNonNull(cargo, "Cargo cannot be null");
        this.cargoMode = Objects.requireNonNull(cargoMode, "Cargo mode cannot be null");
    }

    public Station getStation() {
        return station;
    }

    public CargoType getCargo() {
        return cargo;
    }

    public CargoMode getCargoMode() {
        return cargoMode;
    }

    @Override
    public String toString() {
        return station.getStationName() + " (" + cargo + ", " + cargoMode + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PointOfRoute that = (PointOfRoute) obj;
        return Objects.equals(station, that.station) &&
                cargo == that.cargo &&
                cargoMode == that.cargoMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, cargo, cargoMode);
    }
}
