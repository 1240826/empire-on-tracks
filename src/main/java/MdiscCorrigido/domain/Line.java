package MdiscCorrigido.domain;

/**
 * Represents a connection (line) between two stations.
 * Each line may be electrified or not, and has a distance.
 */
public class Line {
    private final Station a;
    private final Station b;
    private final boolean electrified;
    private final double distance;

    /**
     * Constructs a new Line between two stations.
     *
     * @param a            the first station
     * @param b            the second station
     * @param electrified  {@code true} if the line is electrified; {@code false} otherwise
     * @param distance     the distance of the line
     */
    public Line(Station a, Station b, boolean electrified, double distance) {
        this.a = a;
        this.b = b;
        this.electrified = electrified;
        this.distance = distance;
    }

    /**
     * Returns the first station of this line.
     *
     * @return the first station
     */
    public Station getA() {
        return a;
    }

    /**
     * Returns the second station of this line.
     *
     * @return the second station
     */
    public Station getB() {
        return b;
    }

    /**
     * Indicates whether this line is electrified.
     *
     * @return {@code true} if the line is electrified; {@code false} otherwise
     */
    public boolean isElectrified() {
        return electrified;
    }

    /**
     * Returns the distance of this line.
     *
     * @return the distance
     */
    public double getDistance() {
        return distance;
    }
}
