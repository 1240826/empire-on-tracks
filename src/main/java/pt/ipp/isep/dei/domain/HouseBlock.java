package pt.ipp.isep.dei.domain;

/**
 * Represents a single house block in a city, located at a specific coordinate.
 */
public class HouseBlock {
    private final Location position;

    /**
     * Private constructor. Use {@link #createAt} to instantiate.
     *
     * @param position the coordinates of the house block
     */
    private HouseBlock(Location position) {
        this.position = position;
    }

    /**
     * Factory method to create a HouseBlock at the given position.
     *
     * @param position the coordinates where the block should be placed
     * @return a new HouseBlock instance
     */
    public static HouseBlock createAt(Location position) {
        return new HouseBlock(position);
    }

    /**
     * Returns the location of this house block.
     *
     * @return the block's position
     */
    public Location getPosition() {
        return position;
    }
}
