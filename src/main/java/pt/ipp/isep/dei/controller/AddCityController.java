package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;
import java.util.Objects;

/**
 * Controller for the "Add City" use case.
 * <p>
 * Manages creating a new City, validating inputs, assigning house blocks,
 * and persisting the city to the map repository.
 */
public class AddCityController {
    private Map map;
    private City city;

    /**
     * Default constructor. No initialization until startAddCity() is called.
     */
    public AddCityController() {
        // no-op
    }

    /**
     * Initializes controller state by loading the current map.
     */
    public void startAddCity() {
        this.map = Repositories.getInstance()
                .getMapRepository()
                .getCurrentMap();
    }

    /**
     * Submits the basic city details and validates them.
     *
     * @param name            the city name (non-null, non-empty)
     * @param x               X coordinate of the city center
     * @param y               Y coordinate of the city center
     * @param houseBlocks     number of house blocks (> 0)
     * @return the created City instance
     * @throws IllegalArgumentException if validation fails
     * @throws IllegalStateException    if the city name already exists on the map
     */
    public City submitCityDetails(String name, int x, int y, int houseBlocks) {
        Objects.requireNonNull(name, "city name must not be null");
        city = City.createCity(name, x, y, houseBlocks);
        if (map.isCityNameDuplicate(name)) {
            throw new IllegalStateException("City name is already in use: " + name);
        }
        return city;
    }

    /**
     * Assigns house blocks according to the chosen method.
     * <p>
     * For manual assignment, validates that all positions are within map bounds.
     *
     * @param method     either "automatic" or "manual"
     * @param positions  list of block positions (required if manual)
     * @return the City with assigned blocks
     * @throws IllegalArgumentException if method is unknown or positions invalid
     * @throws IllegalStateException    if manual positions are out of bounds
     */
    public City selectedAssignmentMethod(String method, List<Location> positions) {
        Objects.requireNonNull(method, "assignment method must not be null");
        switch (method.toLowerCase()) {
            case "automatic":
                city.assignHouseBlocksAutomatic();
                break;
            case "manual":
                if (!map.validateCoordinates(positions)) {
                    throw new IllegalStateException("Coordinates out of map bounds");
                }
                city.assignHouseBlocksManually(positions);
                break;
            default:
                throw new IllegalArgumentException("Unknown assignment method: " + method);
        }
        return city;
    }

    /**
     * Saves the newly created city to the map.
     */
    public void confirmCityCreation() {
        map.addCity(city);
    }
}
