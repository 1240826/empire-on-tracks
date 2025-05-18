package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController.StationCargo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a route as an ordered sequence of station+cargo (StationCargo) pairs.
 * The route must have at least two stops: initial and final.
 */
public class Route {
    private final List<StationCargo> stops;

    /**
     * Constructs a route with the given list of stops.
     * @param stops list of StationCargo, must have size >= 2
     * @throws IllegalArgumentException if stops is null or has fewer than 2 elements
     */
    public Route(List<StationCargo> stops) {
        Objects.requireNonNull(stops, "stops cannot be null");
        if (stops.size() < 2) {
            throw new IllegalArgumentException("The route must have at least initial and final stations");
        }
        this.stops = List.copyOf(stops);
    }

    /** Returns the station+cargo pairs in this route (immutable). */
    public List<StationCargo> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /** First stop (station + cargo). */
    public StationCargo getInitialStop() {
        return stops.get(0);
    }

    /** Last stop (station + cargo). */
    public StationCargo getFinalStop() {
        return stops.get(stops.size() - 1);
    }

    /** Sequence of cargo types transported, in the order of the stops. */
    public List<CargoType> getCargoTypes() {
        return stops.stream()
                .map(StationCargo::getCargoType)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return stops.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
