package pt.ipp.isep.dei.Mdisc.domain;

import java.util.Objects;

/**
 * Represents a node (station) in a railway network graph.
 * A station has a unique name and a type (depot, station, or terminal).
 */
public class Station {
    private final String name;
    private final StationType type;

    /**
     * Constructs a new Station with the given name and type.
     *
     * @param name the unique station name (non-null, non-empty)
     * @param type the station type (depot, station, terminal)
     * @throws NullPointerException if name or type is null
     * @throws IllegalArgumentException if name is empty
     */
    public Station(String name, StationType type) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Station name must not be empty");
        }
        this.type = Objects.requireNonNull(type, "type must not be null");
    }

    /**
     * Returns the station's unique name.
     *
     * @return the station name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the station's type.
     *
     * @return the station type
     */
    public StationType getType() {
        return type;
    }

    /**
     * Two stations are equal if they share the same name.
     * We do not override hashCode: equality is identity-based in collections.
     *
     * @param o the other object to compare
     * @return true if o is a Station with the same name
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return name.equals(station.name);
    }

    /**
     * Returns a string representation combining type and name, e.g. "S_Dingwall".
     *
     * @return formatted station identifier
     */
    @Override
    public String toString() {
        return String.format("%s_%s", type.name(), name);
    }
}
