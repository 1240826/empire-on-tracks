package MdiscCorrigido.domain;

/**
 * Representa uma estação de comboios. O nome (ex.: "D8" ou "S2") é guardado tal como vem,
 * sem underscore, e o tipo é inferido pelo primeiro carácter.
 */
public class Station {
    private final String name;
    private final StationType type;

    public Station(String name) {
        this.name = name;
        this.type = StationType.fromName(name);
    }

    public String getName() {
        return name;
    }

    public StationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station other = (Station) o;
        return this.name.equals(other.name) && this.type == other.type;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
