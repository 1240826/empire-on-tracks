package pt.ipp.isep.dei.domain;
import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a city, including its name, location on the map, and associated house blocks.
 */
public class City {

    private String name;
    private Location location;
    private int houseBlocks;
    private List<HouseBlock> houseBlockList;

    /**
     * Constructor to create a city with the specified details.
     *
     * @param name the name of the city
     * @param positionX the X coordinate of the city on the map
     * @param positionY the Y coordinate of the city on the map
     * @param houseBlocks the number of house blocks to assign to the city
     */
    public City(String name, int positionX, int positionY, int houseBlocks) {
        this.name = name;
        this.location = new Location(positionX, positionY);
        this.houseBlocks = houseBlocks;
        this.houseBlockList = new ArrayList<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getHouseBlocks() {
        return houseBlocks;
    }

    public void setHouseBlocks(int houseBlocks) {
        this.houseBlocks = houseBlocks;
    }

    public List<HouseBlock> getHouseBlockList() {
        return houseBlockList;
    }

    /**
     * Adds a house block to the city's house block list.
     *
     * @param houseBlock the house block to be added
     */
    public void addHouseBlock(HouseBlock houseBlock) {
        this.houseBlockList.add(houseBlock);
    }

    /**
     * Validates the city name to ensure it follows the naming rules (e.g., uniqueness).
     *
     * @return true if the name is valid, false otherwise
     */
    public boolean validateName() {
        return true; // Placeholder - to be implemented with proper name uniqueness check
    }

    /**
     * Validates the number of house blocks to ensure it is within acceptable limits.
     *
     * @return true if the house blocks count is valid, false otherwise
     */
    public boolean validateHouseBlocks() {
        return houseBlocks > 0; // Simple validation
    }

    /**
     * Gets the central location of the city, used for automatic house block assignment.
     *
     * @return the center location of the city
     */
    public Location getCityCenter() {
        return location;
    }
}
