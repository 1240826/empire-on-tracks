package pt.ipp.isep.dei.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a route as an ordered sequence of Points-of-Route (station, cargo, cargoMode).
 * The route must have at least two stops (Points-of-Route).
 * The route is circular by default.
 */
public class Route {
    private final List<PointOfRoute> stops;

    /**
     * Constructs a route with the given list of Points-of-Route.
     * @param stops list of PointOfRoute, must have size >= 2
     * @throws IllegalArgumentException if stops is null or has fewer than 2 elements
     */
    public Route(List<PointOfRoute> stops) {
        Objects.requireNonNull(stops, "stops cannot be null");
        if (stops.size() < 2) {
            throw new IllegalArgumentException("The route must have at least initial and final Points-of-Route");
        }
        this.stops = List.copyOf(stops);
    }

    /** Returns the Points-of-Route in this route (immutable). */
    public List<PointOfRoute> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /** First stop (PointOfRoute). */
    public PointOfRoute getInitialStop() {
        return stops.get(0);
    }

    /** Last stop (PointOfRoute). */
    public PointOfRoute getFinalStop() {
        return stops.get(stops.size() - 1);
    }

    /** Sequence of cargo types transported, in the order of the stops. */
    public List<CargoType> getCargoTypes() {
        return stops.stream()
                .map(PointOfRoute::getCargo)
                .collect(Collectors.toUnmodifiableList());
    }

    /** Sequence of stations visited, in the order of the stops. */
    public List<Station> getStations() {
        return stops.stream()
                .map(PointOfRoute::getStation)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return stops.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
