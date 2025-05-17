package pt.ipp.isep.dei.domain;

/**
 * Represents a train carriage.
 */
public class Carriage {
    private String type; // e.g., "cargo" or "passenger"
    private int capacity;

    public Carriage(String type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public int getCapacity() {
        return capacity;
    }
}
