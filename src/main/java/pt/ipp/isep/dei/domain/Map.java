package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Represents the map that contains cities and validates their positions.
 */
public class Map {

    private List<City> cities;

    /**
     * Adds a new city to the map.
     *
     * @param city the city to be added
     */
    public void addCity(City city) {
        cities.add(city);
    }

    /**
     * Checks if a city name is already used in the map.
     *
     * @param name the city name to check
     * @return true if the city name is a duplicate, false otherwise
     */
    public boolean isCityNameDuplicate(String name) {
        return cities.stream().anyMatch(city -> city.getName().equals(name));
    }

    /**
     * Validates if the coordinates of a block are valid and do not cause overbuilding.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     * @return true if the coordinates are valid, false otherwise
     */
    public boolean validatePosition(int x, int y) {
        // Implement the logic to ensure the position doesn't cause overbuilding
        return true; // Simple validation for now
    }

    public List<City> getCities() {
        return cities;
    }
}
