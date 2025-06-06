package pt.ipp.isep.dei.domain;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Point-of-Route: a trio of station, list of cargoes, and cargo mode.
 * Each Point-of-Route corresponds to picking up one or more cargo types at a station,
 * using a specific cargo loading mode.
 */
public class PointOfRoute {
    private final Station station;
    private final List<CargoType> cargoes;
    private final CargoMode cargoMode;

    /**
     * Constructs a Point-of-Route.
     * @param station the station where the train stops
     * @param cargoes the cargo types to be picked up at this station (must not be null or empty)
     * @param cargoMode the loading mode (FULL, HALF, or AVAILABLE)
     * @throws IllegalArgumentException if any argument is null or cargoes is empty
     */
    public PointOfRoute(Station station, Collection<CargoType> cargoes, CargoMode cargoMode) {
        this.station = Objects.requireNonNull(station, "Station cannot be null");
        Objects.requireNonNull(cargoes, "Cargoes cannot be null");
        if (cargoes.isEmpty()) throw new IllegalArgumentException("Cargoes cannot be empty");
        this.cargoes = List.copyOf(cargoes);
        this.cargoMode = Objects.requireNonNull(cargoMode, "Cargo mode cannot be null");
    }

    public Station getStation() {
        return station;
    }

    /** Returns an immutable list of all cargoes for this POR. */
    public List<CargoType> getCargoes() {
        return cargoes;
    }

    public CargoMode getCargoMode() {
        return cargoMode;
    }

    @Override
    public String toString() {
        return station.getStationName() + " (" + cargoes + ", " + cargoMode + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PointOfRoute that = (PointOfRoute) obj;
        return Objects.equals(station, that.station) &&
                Objects.equals(cargoes, that.cargoes) &&
                cargoMode == that.cargoMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(station, cargoes, cargoMode);
    }
}


