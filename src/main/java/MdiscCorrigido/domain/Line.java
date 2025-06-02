package MdiscCorrigido.domain;

/**
 * Representa uma ligação (linha) entre duas estações.
 * Cada linha pode ser electrificada ou não, e tem uma distância.
 */
public class Line {
    private final Station a;
    private final Station b;
    private final boolean electrified;
    private final double distance;

    public Line(Station a, Station b, boolean electrified, double distance) {
        this.a = a;
        this.b = b;
        this.electrified = electrified;
        this.distance = distance;
    }

    public Station getA() {
        return a;
    }

    public Station getB() {
        return b;
    }

    public boolean isElectrified() {
        return electrified;
    }

    public double getDistance() {
        return distance;
    }
}
