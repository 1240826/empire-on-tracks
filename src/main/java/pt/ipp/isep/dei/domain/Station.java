package pt.ipp.isep.dei.domain;

/**
 * Represents a Station in the railway system.
 */
public class Station {

    private String name;
    private int range;  // Example attribute (can be used for coverage area)
    private String type; // e.g., Depot, Station, Terminal
    private int cost;    // Cost of the station

    /**
     * Constructs a Station with the given parameters.
     *
     * @param name  the name of the station
     * @param range the coverage range of the station
     * @param type  the type of station (e.g., "Depot", "Station", "Terminal")
     * @param cost  the cost to build the station
     */
    public Station(String name, int range, String type, int cost) {
        this.name = name;
        this.range = range;
        this.type = type;
        this.cost = cost;
    }

    /**
     * Gets the station's name.
     *
     * @return the station's name
     */
    public String getStationName() {
        return name;
    }

    /**
     * Gets the station's range.
     *
     * @return the range
     */
    public int getRange() {
        return range;
    }

    /**
     * Gets the station's type.
     *
     * @return the type of station
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the cost to build the station.
     *
     * @return the station cost
     */
    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Station: " + name + " (" + type + ")";
    }
}