package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

public class AddCityController {
    private Map map;
    private City city;

    public AddCityController() {
        // nothing here
    }

    public void startAddCity() {
        this.map = Repositories.getInstance().getMapRepository().getCurrentMap();
    }

    public City submitCityDetails(String name, int x, int y, int houseBlocks) throws Exception {
        City c = City.createCity(name, x, y, houseBlocks);
        if (map.isCityNameDuplicate(name)) {
            throw new Exception("City name is already in use");
        }
        this.city = c;
        return this.city;
    }

    public City selectedAssignmentMethod(String method, List<Location> positions) throws Exception {
        if ("automatic".equalsIgnoreCase(method)) {
            city.assignHouseBlocksAutomatic();
        } else if ("manual".equalsIgnoreCase(method)) {
            if (!map.validateCoordinates(positions)) {
                throw new Exception("Coordinates out of bounds");
            }
            city.assignHouseBlocksManually(positions);
        } else {
            throw new IllegalArgumentException("Unknown assignment method: " + method);
        }
        return city;
    }

    public void confirmCityCreation() {
        map.addCity(city);
    }
}
