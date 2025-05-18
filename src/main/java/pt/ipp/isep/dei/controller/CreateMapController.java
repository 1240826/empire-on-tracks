package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.ValueValidator;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller responsible for orchestrating the creation of new Map instances.
 * <p>
 * This controller provides methods to validate user input (map name and dimensions)
 * and to persist a newly created Map into the repository.
 * </p>
 */
public class CreateMapController {

    /**
     * Validator for numeric dimension values.
     */
    private final ValueValidator validator = new ValueValidator();

    /**
     * Repository used to store and retrieve Map objects.
     */
    private final MapRepository repository;

    /**
     * Default constructor.
     * <p>
     * Initializes the {@link MapRepository} by retrieving it from the
     * central {@link Repositories} registry.
     * </p>
     */
    public CreateMapController() {
        this.repository = Repositories.getInstance().getMapRepository();
    }

    /**
     * Validates that the given width and height are positive and within allowed bounds.
     *
     * @param width  the width of the map to validate
     * @param height the height of the map to validate
     * @return {@code true} if both width and height are valid; {@code false} otherwise
     */
    public boolean validateDimensions(int width, int height) {
        return validator.validateWidthLength(width, height);
    }

    /**
     * Validates the proposed map name.
     * <p>
     * A valid name must be non-null, non-blank, and must not duplicate
     * an existing map name in the repository.
     * </p>
     *
     * @param name the map name to validate
     * @return {@code true} if the name is valid and not already in use; {@code false} otherwise
     */
    public boolean validateMapName(String name) {
        List<String> existingNames = repository.getMapsNames();
        return name != null
                && !name.trim().isEmpty()
                && !existingNames.contains(name);
    }

    /**
     * Creates and persists a new Map with the specified attributes.
     * <p>
     * This method constructs a new {@link Map} instance and saves it
     * into the {@link MapRepository}.
     * </p>
     *
     * @param name   the unique name of the new map
     * @param width  the width dimension of the map (must be positive)
     * @param height the height dimension of the map (must be positive)
     */
    public void createMap(String name, int width, int height) {
        Map map = new Map(name, width, height);
        repository.saveMap(map);
    }

}
