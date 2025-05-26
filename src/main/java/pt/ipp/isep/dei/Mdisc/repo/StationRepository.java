// src/main/java/org/example/repo/StationRepository.java
package pt.ipp.isep.dei.Mdisc.repo;

import pt.ipp.isep.dei.Mdisc.domain.Station;
import pt.ipp.isep.dei.Mdisc.domain.StationType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Repository for loading {@link Station} instances from a CSV file.
 * <p>
 * Expects a single line of input where each token is formatted as:
 * <pre>
 *   TYPE_name
 * </pre>
 * (e.g., "S_Dingwall;D_Helmsdale;T_Inverness").
 * Tokens are separated by semicolons. The repository parses each token,
 * creates a {@link Station}, and returns a sorted list by type then name.
 */
public class StationRepository {
    private final String stationsFile;

    /**
     * @param stationsFile path to the stations definition file
     */
    public StationRepository(String stationsFile) {
        this.stationsFile = Objects.requireNonNull(stationsFile, "stationsFile must not be null");
    }

    /**
     * Loads and returns all Station instances from the configured file.
     * <p>
     * Parsing logic:
     * <ol>
     *   <li>Read the first line of the file (if any).</li>
     *   <li>Split line at semicolons into tokens.</li>
     *   <li>For each token, split at the first underscore into type code and name.</li>
     *   <li>Create a {@link StationType} from the code and a new {@link Station}.</li>
     *   <li>Ignore malformed tokens.</li>
     *   <li>Sort the resulting list by station type (ordinal) then name.</li>
     * </ol>
     *
     * @return sorted List of Station instances, empty if file is empty
     * @throws FileNotFoundException if the stations file cannot be opened
     */
    public List<Station> loadAll() throws FileNotFoundException {
        List<Station> stations = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(stationsFile))) {
            if (!sc.hasNextLine()) {
                return stations;
            }
            String line = sc.nextLine();
            for (String token : line.split(";")) {
                if (token.length() >= 2) {
                    char typeChar = token.charAt(0);
                    String name = token.substring(1);
                    try {
                        StationType type = StationType.valueOf(String.valueOf(typeChar));
                        stations.add(new Station(name, type));
                    } catch (IllegalArgumentException e) {
                        // Ignore unknown station type
                    }
                }
            }
        }
        stations.sort(Comparator
                .comparing(Station::getType)
                .thenComparing(Station::getName)
        );
        return stations;
    }
}