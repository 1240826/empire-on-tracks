package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
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
    private final List<PointOfRoute> pointsOfRoute;

    /**
     * Constructs a route with the given list of Points-of-Route.
     * @param pointsOfRoute list of PointOfRoute, must have size >= 2
     * @throws IllegalArgumentException if stops is null or has fewer than 2 elements
     */
    public Route(List<PointOfRoute> pointsOfRoute) {
        Objects.requireNonNull(pointsOfRoute, "stops cannot be null");
        if (pointsOfRoute.size() < 2) {
            throw new IllegalArgumentException("The route must have at least initial and final Points-of-Route");
        }
        this.pointsOfRoute = List.copyOf(pointsOfRoute);
    }

    
    /** First stop (PointOfRoute). */
    public PointOfRoute getInitialStop() {
        return pointsOfRoute.get(0);
    }

    /** Last stop (PointOfRoute). */
    public PointOfRoute getFinalStop() {
        return pointsOfRoute.get(pointsOfRoute.size() - 1);
    }

    /** Sequence of cargo types transported, in the order of the stops. */
    public List<CargoType> getCargoTypes() {
        return pointsOfRoute.stream()
                .flatMap(por -> por.getCargoes().stream())
                .collect(Collectors.toUnmodifiableList());
    }

    /** Sequence of stations visited, in the order of the stops. */
    public List<Station> getStations() {
        return pointsOfRoute.stream()
                .map(PointOfRoute::getStation)
                .collect(Collectors.toUnmodifiableList());
    }
    public List<PointOfRoute> getPointsOfRoute() {
        return pointsOfRoute;
    }

    /** Returns the number of stops in the route (equals number of PointOfRoute). */
    public int getNumberOfStops() {
        return pointsOfRoute.size();
    }

    /**
     * Returns a List<Integer> where each entry is the number of cargo types
     * to be picked up at each stop (i.e., number of carriages needed per stop).
     */
    public List<Integer> getCarriageCountsPerStop() {
        List<Integer> result = new ArrayList<>();
        for (PointOfRoute por : pointsOfRoute) {
            result.add(por.getCargoes().size());
        }
        return result;
    }

    @Override
    public String toString() {
        return pointsOfRoute.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
