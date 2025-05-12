package pt.ipp.isep.dei.domain;

/**
 * Represents a house block in the city.
 */
public class HouseBlock {

    private Location location;

    /**
     * Creates a new house block at a given location.
     *
     * @param location the location of the house block
     * @return a new HouseBlock at the specified location
     */
    public static HouseBlock createAt(Location location) {
        HouseBlock houseBlock = new HouseBlock();
        houseBlock.location = location;
        return houseBlock;
    }

    // Getter
    public Location getLocation() {
        return location;
    }
}
