package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.ui.console.AddCityUI;

import java.util.List;
import java.util.Random;

/**
 * Controller responsible for handling the process of adding a new city.
 */
public class AddCityController {

    private MapRepository mapRepository;
    private City city;

    /**
     * Initializes the controller with a MapRepository instance.
     */
    public AddCityController() {
        this.mapRepository = Repositories.getInstance().getMapRepository();
    }

    /**
     * Starts the process of adding a new city, initializing a new city with default values.
     */
    public void startAddCity() {
        this.city = new City("", 0, 0, 0); // Initialize with empty or default values
    }

    /**
     * Submits the details of the city and validates them.
     *
     * @param name the name of the city
     * @param positionX the X coordinate of the city
     * @param positionY the Y coordinate of the city
     * @param houseBlocks the number of house blocks for the city
     */
    public void submitCityDetails(String name, int positionX, int positionY, int houseBlocks) {
        city.setName(name);
        city.setPositionX(positionX);
        city.setPositionY(positionY);
        city.setHouseBlocks(houseBlocks);

        // Validate city details before proceeding
        if (!city.validateName()) {
            AddCityUI.showWarning("City name is invalid or already in use.");
            return;
        }

        if (!city.validateHouseBlocks()) {
            AddCityUI.showWarning("Invalid number of house blocks.");
            return;
        }

        // Check if the city name is unique in the map
        if (mapRepository.getCurrentMap().isCityNameDuplicate(name)) {
            AddCityUI.showWarning("City name is already in use.");
            return;
        }

        // Proceed with the assignment method
        city.requestAssignmentMethod();
    }

    /**
     * Selects the assignment method (automatic or manual) for placing the house blocks.
     *
     * @param method the assignment method ("automatic" or "manual")
     * @param positions the positions of the house blocks (only for manual assignment)
     */
    public void selectedAssignmentMethod(String method, List<Location> positions) {
        if (method.equals("automatic")) {
            // Automatically assign house blocks around the city center
            Location center = city.getCityCenter();
            for (int i = 0; i < city.getHouseBlocks(); i++) {
                Location newLocation = generateNormalDistributionPosition(center);
                if (newLocation != null) {
                    HouseBlock houseBlock = HouseBlock.createAt(newLocation);
                    city.addHouseBlock(houseBlock);
                }
            }
        } else if (method.equals("manual")) {
            // Validate and assign house blocks manually based on user input
            if (!mapRepository.getCurrentMap().validateCoordinates(positions)) {
                AddCityUI.showWarning("Coordinates out of bounds.");
                return;
            }
            city.assignHouseBlocksManually(positions);
        }

        AddCityUI.showSuccess("City added successfully.");
    }

    /**
     * Generates a position using a normal distribution around the city center.
     *
     * @param center the city center location
     * @return a valid location for placing a house block, or null if no valid position is found
     */
    private Location generateNormalDistributionPosition(Location center) {
        Random random = new Random();
        double radius = 5; // Radius for the random distribution
        double xOffset = random.nextGaussian() * radius;
        double yOffset = random.nextGaussian() * radius;

        int newX = center.getX() + (int) xOffset;
        int newY = center.getY() + (int) yOffset;

        // Ensure the position is valid within the map's constraints
        if (mapRepository.getCurrentMap().validatePosition(newX, newY)) {
            return new Location(newX, newY);
        }

        // Retry the generation if the position is invalid
        return generateNormalDistributionPosition(center);
    }

    /**
     * Confirms the city creation and adds it to the current map.
     */
    public void confirmCityCreation() {
        Map map = mapRepository.getCurrentMap();
        map.addCity(city);
        AddCityUI.showSuccess("City has been successfully added to the map.");
    }
}

