package pt.ipp.isep.dei.domain;

/**
 * Represents a house block in the city.
 */
public class HouseBlock {
    private Location position;

    public HouseBlock(Location position) {
        this.position = position;
    }

    /**
     * Creates a HouseBlock at the specified position.
     *
     * @param position the location of the house block
     * @return a new HouseBlock instance
     */
    public static HouseBlock createAt(Location position) {
        HouseBlock hb = new HouseBlock(position);
        hb.position = position;
        return hb;
    }

    /**
     * Returns the location of the house block.
     *
     * @return the Location of this house block
     */
    public Location getPosition() {
        return position;
    }
}

