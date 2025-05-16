package pt.ipp.isep.dei.domain;

/**
 * Represents a location on the map, defined by X and Y coordinates.
 */
public class Location {
    private int x;
    private int y;

    /**
     * Constructs a Location with specified x and y coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X coordinate.
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }
}