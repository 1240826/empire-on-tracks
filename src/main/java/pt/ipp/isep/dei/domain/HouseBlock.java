package pt.ipp.isep.dei.domain;

/**
 * Represents a single house block in a city, located at a specific coordinate.
 */
public class HouseBlock {
    private Location location;

    public HouseBlock(int positionX, int positionY) {
        this.location = new Location(positionX, positionY);
    }

    public Location getLocation() {
        return location;
    }
}
