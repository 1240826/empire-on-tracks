package MdiscCorrigido.domain;

/**
 * Represents a railway station. The name (e.g. "D8" or "S2") is stored as provided,
 * without underscores, and the station type is inferred from the first character.
 */
public class Station {
    private final String name;
    private final StationType type;

    /**
     * Constructs a new Station with the given name.
     * The station type is determined by the first character of the name.
     *
     * @param name the name of the station (e.g. "D8" or "S2"), without underscores
     */
    public Station(String name) {
        this.name = name;
        this.type = StationType.fromName(name);
    }

    /**
     * Returns the name of this station.
     *
     * @return the station name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of this station.
     *
     * @return the station type (D, S, or T)
     */
    public StationType getType() {
        return type;
    }

    /**
     * Returns a string representation of this station,
     * which is simply its name.
     *
     * @return the station name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares this station to another object for equality.
     * Two stations are considered equal if they have the same name and type.
     *
     * @param o the object to compare with
     * @return {@code true} if the other object is a Station with the same name and type; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station other = (Station) o;
        return this.name.equals(other.name) && this.type == other.type;
    }

    /**
     * Returns the hash code for this station, based on its name.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
