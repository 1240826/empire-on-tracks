package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a route as an ordered list of station–cargo assignments.
 * Now implemented as a generic container so it can hold any type of elements (e.g., String or StationCargo).
 *
 * @param <T> the type of elements in the route list
 */
public class Route<T> {

    private final List<T> stationCargoList;

    /**
     * Constructs a new Route.
     *
     * @param stationCargoList list of elements; must not be null
     * @throws IllegalArgumentException if stationCargoList is null
     */
    public Route(List<T> stationCargoList) {
        if (stationCargoList == null) {
            throw new IllegalArgumentException("stationCargoList cannot be null");
        }
        this.stationCargoList = new ArrayList<>(stationCargoList);
    }

    /**
     * Returns an unmodifiable view of the route elements.
     *
     * @return list of elements (non-null)
     */
    public List<T> getStationCargoList() {
        return Collections.unmodifiableList(stationCargoList);
    }

    @Override
    public String toString() {
        return stationCargoList.toString();
    }
}
