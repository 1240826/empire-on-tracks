// src/main/java/org/example/repo/StationRepository.java
package pt.ipp.isep.dei.Mdisc.repo;

import pt.ipp.isep.dei.Mdisc.domain.Station;
import pt.ipp.isep.dei.Mdisc.domain.StationType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class StationRepository {
    private final String stationsFile;

    public StationRepository(String stationsFile) {
        this.stationsFile = stationsFile;
    }

    public List<Station> loadAll() throws FileNotFoundException {
        List<Station> stations = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(stationsFile))) {
            if (!sc.hasNextLine()) return stations;
            for (String tok : sc.nextLine().split(";")) {
                String[] p = tok.split("_", 2);
                if (p.length == 2) {
                    StationType type = StationType.valueOf(p[0]);
                    stations.add(new Station(p[1], type));
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
