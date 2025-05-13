package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.ValueValidator;
import pt.ipp.isep.dei.repository.RepositoryMap;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;


public class CreateMapController {
    private final ValueValidator validator = new ValueValidator();
    private final RepositoryMap repository;

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
