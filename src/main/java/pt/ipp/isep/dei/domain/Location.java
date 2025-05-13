package pt.ipp.isep.dei.domain;

/**
 * Represents a location on the map, defined by X and Y coordinates.
 */
public class Location {

    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * Computes a new position for a block based on the center location,
     * used for automatic assignment.
     *
     * @param center the central location
     * @return a new location for the block
     */
    public static Location computeBlockPosition(Location center) {
        // Simplified position calculation, can be enhanced
        return new Location(center.getX() + 1, center.getY() + 1); // Example logic
    }
}
