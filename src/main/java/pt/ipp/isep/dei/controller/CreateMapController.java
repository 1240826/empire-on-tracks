package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.ValueValidator;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller responsible for managing map creation and validation.
 *
 * Provides methods to validate map dimensions, check map name availability,
 * and create new maps in the repository.
 */
public class CreateMapController {
    private final ValueValidator validator = new ValueValidator();
    private final MapRepository repository;

    public CreateMapController() {
        this.repository = Repositories.getInstance().getMapRepository();
    }

    public boolean validateDimensions(int width, int height) {
        return validator.validateWidthLength(width, height);
    }

    public boolean validateMapName(String name) {
        List<String> existingNames = repository.getMapsNames();
        return name != null && !name.trim().isEmpty() && !existingNames.contains(name);
    }

    public void createMap(String name, int width, int height) {
        Map map = new Map(name, width, height);
        repository.saveMap(map);
    }




}
