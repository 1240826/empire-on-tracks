package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.ValueValidator;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;

/**
 * Controller for the “Create Map” use-case, implementing a
 * two-phase workflow exactly as in the sequence diagram.
 */
public class CreateMapController {

    private final ValueValidator validator = new ValueValidator();
    private final MapRepository  repository =
            Repositories.getInstance().getMapRepository();

    /** Validates that both dimensions are positive. */
    public boolean validateDimensions(int width, int height) {
        return validator.validateWidthLength(width, height);
    }

    /** Validates that name is non-empty and unique. */
    public boolean validateMapName(String name) {
        return name != null &&
                !name.trim().isEmpty() &&
                repository.isMapNameUnique(name);
    }

    /**
     * Phase 1 – build a draft Map instance; throws if invalid.
     *
     * @throws IllegalArgumentException if name or dimensions are invalid
     */
    public Map createMapDraft(String name, int width, int height) {
        if (!validateMapName(name) || !validateDimensions(width, height)) {
            throw new IllegalArgumentException("Invalid name or dimensions");
        }
        return repository.createMap(name, width, height);
    }

    /** Phase 2 – persist the draft Map after confirmation. */
    public void saveMap(Map map) {
        repository.saveMap(map);
    }

    /**
     * Legacy single-step API for backwards compatibility.
     * Delegates to the new two-step methods.
     */
    @Deprecated
    public void createMap(String name, int width, int height) {
        Map draft = createMapDraft(name, width, height);
        saveMap(draft);
    }
}
