package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Represents a route as an ordered list of station-cargo pairs.
 * <p>
 * This class is a general-purpose container for associating a route with a sequence of stops,
 * each specifying a cargo type.
 * </p>
 * <p>
 * The {@code stationCargoList} must not be {@code null}; passing {@code null} will result
 * in an {@link IllegalArgumentException}.
 * </p>
 */

public class Route {

    /**
     * The ordered list of station-cargo assignments for this route.
     */
    private final List<?> stationCargoList;

    /**
     * Constructs a new {@code Route} with the provided station-cargo assignment list.
     *
     * @param stationCargoList the ordered list representing the route; must not be {@code null}
     * @throws IllegalArgumentException if {@code stationCargoList} is {@code null}
     */

    public Route(List<?> stationCargoList) {
        if (stationCargoList == null) {
            throw new IllegalArgumentException("stationCargoList cannot be null");
        }
        this.stationCargoList = stationCargoList;
    }

    /**
     * Returns the list of station-cargo assignments for this route.
     *
     * @return the (non-null) ordered list of assignments
     */
    public List<?> getStationCargoList() {
        return stationCargoList;
    }
}
