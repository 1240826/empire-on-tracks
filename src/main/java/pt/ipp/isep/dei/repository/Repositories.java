package pt.ipp.isep.dei.repository;
/**
 * Inspired by Singleton pattern to manage all repositories in the application.
 *
 * The Repositories class works as a Singleton. It defines the getInstance method that serves as an alternative
 * to the constructor and lets client classes access the same instance of this class over and over.
 */
public class Repositories {
    private static Repositories instance;
    private final RepositoryMap repositoryMap;
    private final IndustryRepository industryRepository;
    /**
     * The Singleton's constructor should always be private to prevent direct construction calls with the new operator.
     */
    private Repositories() {
        repositoryMap = new RepositoryMap();  // Initialize the MapRepository
        industryRepository = IndustryRepository.getInstance();
    }

    /**
     * This is the static method that controls the access to the singleton instance.
     * On the first run, it creates a singleton object and places it into the static attribute.
     * On subsequent runs, it returns the existing object stored in the static attribute.
     */
    public static Repositories getInstance() {
        if (instance == null) {
            synchronized (Repositories.class) {
                instance = new Repositories();
            }
        }
        return instance;
    }

    /**
     * Returns the MapRepository instance.
     *
     * @return the MapRepository instance
     */
    public RepositoryMap getMapRepository() {
        return repositoryMap;
    }
    public IndustryRepository getIndustryRepository(){
        return industryRepository;
    }
}
