package pt.ipp.isep.dei.domain;

import java.util.Objects;
import java.util.Random;

/**
 * Represents a 2D coordinate location.
 * Provides utility to compute a random offset position around a center.
 */
public class Location {
    private final int x;
    private final int y;
    private static final Random rand = new Random();
    private static final double STD_DEV = 10.0;

    /**
     * Constructs a Location with given coordinates.
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
     * @return the X value
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return the Y value
     */
    public int getY() {
        return y;
    }

    /**
     * Computes a new Location by adding normal-distributed offsets
     * to the center's coordinates. Useful for automatic houseblock placement.
     *
     * @param center the central Location
     * @return a new Location offset from center
     */
    public static Location computeBlockPosition(Location center) {
        Objects.requireNonNull(center, "center must not be null");

        int newX, newY;
        do {
            int dx = (int) Math.round(rand.nextGaussian() * STD_DEV);
            int dy = (int) Math.round(rand.nextGaussian() * STD_DEV);
            newX = center.x + dx;
            newY = center.y + dy;
        } while (newX < 0 || newY < 0);

        return new Location(newX, newY);
    }


    /**
     * Returns a string representation of this Location in the form "(x, y)".
     *
     * @return the string format of coordinates
     */
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
}
