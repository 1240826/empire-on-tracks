package pt.ipp.isep.dei.domain;
import java.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a City in the map with a name, position, and a collection of house blocks.
 */
public class City {

    private String name;
    private int positionX;
    private int positionY;
    private int houseBlocksCount;
    private List<HouseBlock> houseBlocks;

    private static final double STD_DEV = 1.0; // Standard deviation for Gaussian distribution

    private final Random random = new Random();

    /**
     * Creates a new City instance.
     *
     * @param name             The name of the city.
     * @param positionX        The X coordinate of the city's center position.
     * @param positionY        The Y coordinate of the city's center position.
     * @param houseBlocksCount The number of house blocks to be assigned.
     */
    public City(String name, int positionX, int positionY, int houseBlocksCount) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.houseBlocksCount = houseBlocksCount;
        this.houseBlocks = new ArrayList<>();
    }

    /**
     * Gets the name of the city.
     *
     * @return The city's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the city.
     *
     * @param name The new name of the city.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the X coordinate of the city's center.
     *
     * @return The X position.
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Sets the X coordinate of the city's center.
     *
     * @param positionX The new X position.
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    /**
     * Gets the Y coordinate of the city's center.
     *
     * @return The Y position.
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Sets the Y coordinate of the city's center.
     *
     * @param positionY The new Y position.
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    /**
     * Gets the number of house blocks in the city.
     *
     * @return The number of house blocks.
     */
    public int getHouseBlocksCount() {
        return houseBlocksCount;
    }

    /**
     * Sets the number of house blocks to assign to the city.
     *
     * @param houseBlocksCount The number of house blocks.
     */
    public void setHouseBlocksCount(int houseBlocksCount) {
        this.houseBlocksCount = houseBlocksCount;
    }

    /**
     * Gets a list of house blocks that belong to the city.
     *
     * @return List of HouseBlock instances.
     */
    public List<HouseBlock> getHouseBlocks() {
        return new ArrayList<>(houseBlocks);
    }

    /**
     * Adds a house block to the city.
     *
     * @param houseBlock The HouseBlock to add.
     */
    public void addHouseBlock(HouseBlock houseBlock) {
        if (houseBlock != null) {
            this.houseBlocks.add(houseBlock);
        }
    }

    /**
     * Validates if the city name meets expected criteria (e.g., not null or empty).
     *
     * @return true if the name is valid; false otherwise.
     */
    public boolean validateName() {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Validates if the number of house blocks is positive.
     *
     * @return true if houseBlocksCount is positive; false otherwise.
     */
    public boolean validateHouseBlocks() {
        return houseBlocksCount > 0;
    }

    /**
     * Returns the city's center position as a Location object.
     *
     * @return Location representing the city's center.
     */
    public Location getCityCenter() {
        return new Location(positionX, positionY);
    }

    /**
     * Automatically assigns house blocks around the city's center position
     * in a simple square pattern.
     * <p>
     * The method assigns houseBlocksCount house blocks by placing them
     * in consecutive positions starting from the city's center,
     * moving rightwards and downwards.
     */
    public void assignHouseBlocksAutomatic() {
        houseBlocks.clear();

        int blocksAssigned = 0;
        int startX = positionX;
        int startY = positionY;

        int sideLength = (int) Math.ceil(Math.sqrt(houseBlocksCount)); // side length of square area

        for (int dx = 0; dx < sideLength && blocksAssigned < houseBlocksCount; dx++) {
            for (int dy = 0; dy < sideLength && blocksAssigned < houseBlocksCount; dy++) {
                int blockX = startX + dx;
                int blockY = startY + dy;

                if (blockX >= 0 && blockY >= 0) {
                    houseBlocks.add(new HouseBlock(new Location(blockX, blockY)));
                    blocksAssigned++;
                }
            }
        }
    }

    /**
     * Allows manual assignment of house blocks given a list of positions.
     *
     * @param positions List of Location objects where house blocks will be placed.
     */
    public void assignHouseBlocksManually(List<Location> positions) {
        houseBlocks.clear();
        if (positions == null) {
            return;
        }
        for (Location loc : positions) {
            houseBlocks.add(new HouseBlock(loc));
        }
    }
}
