package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.LocomotiveCatalog;
import pt.ipp.isep.dei.domain.Map;

/**
 * Inspired by the Singleton pattern to manage all repositories in the application.
 *
 * Provides access to:
 *  - MapRepository
 *  - IndustryRepository
 *  - LocomotiveCatalog
 *  - ScenarioRepository
 */
public class Repositories {

    private static Repositories instance;

    private final MapRepository      repositoryMap;
    private final IndustryRepository industryRepository;
    private final LocomotiveCatalog  locomotiveCatalog   = new LocomotiveCatalog();
    private final ScenarioRepository scenarioRepository   = new ScenarioRepository();

    /** Prevent direct instantiation. */
    private Repositories() {
        this.repositoryMap      = new MapRepository();
        this.industryRepository = IndustryRepository.getInstance();
    }

    /**
     * Returns the singleton instance, creating it if necessary.
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                if (instance == null) {
                    instance = new Repositories();
                }
            }
        }
        return instance;
    }

    /**
     * @return the MapRepository
     */
    public MapRepository getMapRepository() {
        return repositoryMap;
    }

    /**
     * @return the IndustryRepository
     */
    public IndustryRepository getIndustryRepository() {
        return industryRepository;
    }

    /**
     * @return the LocomotiveCatalog
     */
    public LocomotiveCatalog getLocomotiveCatalog() {
        return locomotiveCatalog;
    }

    /**
     * @return the ScenarioRepository
     */
    public ScenarioRepository getScenarioRepository() {
        return scenarioRepository;
    }
}
