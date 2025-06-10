package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller for the "Add City" use case.
 * <p>
 * Manages creating a new City, validating inputs, assigning house blocks,
 * and persisting the city to the map repository.
 */
public class AddCityController {
    private Map map;

    public AddCityController(Map map) {
        this.map = map;
    }

    // Verifica se o nome da cidade já existe
    public boolean isCityNameDuplicate(String name) {
        return map.cityExists(name);
    }

    // Cria uma cidade nova com base nos dados fornecidos
    public City createCity(String name, int positionX, int positionY, int houseBlockCount, String assignmentMethod,
                           List<Integer> manualPosX, List<Integer> manualPosY) {
        if (isCityNameDuplicate(name)) {
            throw new IllegalArgumentException("City name already exists.");
        }

        // Criação da cidade no mapa, usando o método adequado com base no método de atribuição de blocos
        return map.create(name, positionX, positionY, houseBlockCount, assignmentMethod, manualPosX, manualPosY);
    }

    // Remove uma cidade do mapa, se existir
    public boolean removeCity(String name) {
        if (isCityNameDuplicate(name)) {
            map.removeCity(name);
            return true;
        }
        return false;
    }
}
