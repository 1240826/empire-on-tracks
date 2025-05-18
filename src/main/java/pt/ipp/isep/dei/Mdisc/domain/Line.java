package pt.ipp.isep.dei.Mdisc.domain;

import java.util.Objects;

/**
 * Represents an undirected connection (edge) between two {@link Station} nodes
 * in a railway graph, with an attribute indicating whether the line is electrified.
 */
public class Line {
    private final Station from;
    private final Station to;
    private final boolean electrified;

    /**
     * Constructs a new Line connecting two stations.
     *
     * @param from          the originating station (non-null)
     * @param to            the destination station (non-null)
     * @param electrified   true if this line is equipped with electrification
     * @throws NullPointerException if {@code from} or {@code to} is null
     */
    public Line(Station from, Station to, boolean electrified) {
        this.from = Objects.requireNonNull(from, "from station must not be null");
        this.to = Objects.requireNonNull(to,   "to station must not be null");
        this.electrified = electrified;
    }

    /**
     * Returns the originating station of this line.
     *
     * @return the "from" station
     */
    public Station getFrom() {
        return from;
    }

    /**
     * Returns the destination station of this line.
     *
     * @return the "to" station
     */
    public Station getTo() {
        return to;
    }

    /**
     * Indicates whether this line is electrified.
     *
     * @return true if the line is electrified, false otherwise
     */
    public boolean isElectrified() {
        return electrified;
    }

    /**
     * Returns a string representation of the Line.
     *
     * @return a string in the format from--(electrified?)-->to
     */
    @Override
    public String toString() {
        return String.format("%s --%selectrified--> %s",
                from.getName(), electrified ? "" : "non-", to.getName());
    }
}
