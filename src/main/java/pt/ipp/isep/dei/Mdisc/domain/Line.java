package pt.ipp.isep.dei.Mdisc.domain;

public class Line {
    private final Station from;
    private final Station to;
    private final boolean electrified;

    public Line(Station from, Station to, boolean electrified) {
        this.from = from;
        this.to = to;
        this.electrified = electrified;
    }

    public Station getFrom() {
        return from;
    }

    public Station getTo() {
        return to;
    }

    public boolean isElectrified() {
        return electrified;
    }
}