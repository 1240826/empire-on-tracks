package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a City with a central location and multiple house blocks.
 * <p>
 * A City has a name, a center coordinate, and a specified number of house blocks.
 * House blocks can be assigned either automatically (random distribution)
 * or manually (user-provided positions).
 */
public class City {
    private final String name;
    private final int centerX;
    private final int centerY;
    private final int houseBlocksCount;
    private final List<HouseBlock> houseBlocks = new ArrayList<>();
    private final Location center;

    /**
     * Private constructor. Use {@link #createCity} to instantiate.
     */
    City(String name, int centerX, int centerY, int houseBlocksCount) {
        this.name = name;
        this.centerX = centerX;
        this.centerY = centerY;
        this.houseBlocksCount = houseBlocksCount;
        this.center = new Location(centerX, centerY);
    }

    /**
     * Factory method to create and validate a new City.
     *
     * @param name              non-null, non-empty city name
     * @param centerX           X coordinate of the city center
     * @param centerY           Y coordinate of the city center
     * @param houseBlocksCount  desired number of house blocks (must be > 0)
     * @return a new valid City instance
     * @throws IllegalArgumentException if validation fails
     */
    public static City createCity(String name, int centerX, int centerY, int houseBlocksCount) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid city name: " + name);
        }
        if (houseBlocksCount <= 0) {
            throw new IllegalArgumentException("Invalid number of house blocks: " + houseBlocksCount);
        }
        if (centerX <= 0 || centerY <= 0) {
            throw new IllegalArgumentException("City center coordinates must be positive and not (0,0). Provided: (" + centerX + ", " + centerY + ")");
        }
        return new City(name, centerX, centerY, houseBlocksCount);
    }

    /**
     * Assigns house blocks automatically around the center using a random distribution.
     */
    public void assignHouseBlocksAutomatic() {
        for (int i = 0; i < houseBlocksCount; i++) {
            Location pos = Location.computeBlockPosition(center);
            houseBlocks.add(HouseBlock.createAt(pos));
        }
    }

    /**
     * Assigns house blocks at the specified positions.
     *
     * @param positions list of positions; size must equal the house block count
     * @throws IllegalArgumentException if the list size does not match
     */
    public void assignHouseBlocksManually(List<Location> positions) {
        Objects.requireNonNull(positions, "positions must not be null");
        if (positions.size() != houseBlocksCount) {
            throw new IllegalArgumentException(
                    "Number of positions (" + positions.size() + ") must match house block count (" + houseBlocksCount + ")");
        }
        for (Location pos : positions) {
            houseBlocks.add(HouseBlock.createAt(pos));
        }
    }

    // Getters

    /** @return the city name */
    public String getName() {
        return name;
    }

    /** @return the X coordinate of the city center */
    public int getCenterX() {
        return centerX;
    }

    /** @return the Y coordinate of the city center */
    public int getCenterY() {
        return centerY;
    }

    /** @return the Location object representing the center */
    public Location getCenter() {
        return center;
    }

    /** @return the desired number of house blocks */
    public int getHouseBlocksCount() {
        return houseBlocksCount;
    }

    /**
     * Returns an immutable list of the assigned house blocks.
     *
     * @return list of HouseBlock instances
     */
    public List<HouseBlock> getHouseBlocks() {
        return new ArrayList<>(houseBlocks);
    }
}
