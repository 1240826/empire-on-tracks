package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository responsible for storing and managing multiple Map instances.
 * <p>
 * Maintains a list of maps and tracks a “current” map for user operations.
 * </p>
 */
public class MapRepository {

    /** Internal list of all stored maps. */
    private List<Map> maps;

    /** The currently selected map in the repository. */
    private Map currentMap;

    /**
     * Constructs a MapRepository with a default initial map.
     * <p>
     * Creates a new default Map and sets it as the current map.
     * </p>
     */
    public MapRepository() {
        this.maps = new ArrayList<>();
        this.currentMap = new Map();  // Initial default map
        this.maps.add(currentMap);
    }

    /**
     * Returns the currently active map.
     *
     * @return the current {@link Map}
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Sets the specified map as the current map.
     *
     * @param map the {@link Map} to make active
     */
    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    /**
     * Adds a new map to the repository without changing the current map.
     *
     * @param map the {@link Map} to add
     */
    public void addMap(Map map) {
        this.maps.add(map);
    }

    /**
     * Saves the given map by adding it to the repository and
     * setting it as the current map.
     *
     * @param map the {@link Map} to save and activate
     */
    public void saveMap(Map map) {
        addMap(map);
        setCurrentMap(map);
    }

    /**
     * Retrieves the names of all maps stored in the repository.
     *
     * @return a {@code List<String>} of map names
     */
    public List<String> getMapsNames() {
        List<String> names = new ArrayList<>();
        for (Map m : maps) {
            names.add(m.getName());
        }
        return names;
    }
}
