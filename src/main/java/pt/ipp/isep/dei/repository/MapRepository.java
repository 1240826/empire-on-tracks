package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for managing maps and cities.
 */
public class MapRepository {

    private List<Map> maps;
    private Map currentMap;

    /**
     * Initializes the repository with a default or empty map.
     */
    public MapRepository() {
        this.maps = new ArrayList<>();
        this.currentMap = new Map();  // Initial empty or default map
        this.maps.add(currentMap);
    }

    /**
     * Gets the current map from the repository.
     *
     * @return the current map
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Sets the current map to the specified map.
     *
     * @param map the map to be set as the current map
     */
    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    /**
     * Adds a new map to the repository.
     *
     * @param map the map to be added
     */
    public void addMap(Map map) {

        this.maps.add(map);
    }

    public void saveMap(Map map) {
        addMap(map);
        setCurrentMap(map);
    }

    public List<String> getMapsNames() {
        List<String> names = new ArrayList<>();
        for (Map m : maps) {
            names.add(m.getName());
        }
        return names;
    }
}

