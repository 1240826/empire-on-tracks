package pt.ipp.isep.dei.Mdisc.domain;

import java.util.Objects;

public class Station {
    private final String name;
    private final StationType type;

    public Station(String name, StationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public StationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.name() + "_" + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}