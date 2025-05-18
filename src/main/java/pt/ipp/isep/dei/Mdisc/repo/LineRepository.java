package pt.ipp.isep.dei.Mdisc.repo;

import pt.ipp.isep.dei.Mdisc.domain.Line;
import pt.ipp.isep.dei.Mdisc.domain.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Repository responsible for loading Line instances from a file.
 * <p>
 * Each line in the file is expected to have the format:
 * <pre>
 *   stationA;stationB;electrifiedFlag;distance
 * </pre>
 * where:
 * <ul>
 *   <li><b>stationA</b> and <b>stationB</b> are station identifiers matching those in the provided station list,</li>
 *   <li><b>electrifiedFlag</b> is "1" for electrified lines or "0" otherwise,</li>
 *   <li><b>distance</b> is the positive real-valued distance between the stations.</li>
 * </ul>
 * Only lines matching the {@code onlyElectrified} filter will be returned.
 */
public class LineRepository {
    private final String linesFile;
    private final boolean onlyElectrified;

    /**
     * Constructs a LineRepository.
     *
     * @param linesFile        path to the lines definition file
     * @param onlyElectrified  if true, only electrified lines are loaded
     */
    public LineRepository(String linesFile, boolean onlyElectrified) {
        this.linesFile = Objects.requireNonNull(linesFile, "linesFile must not be null");
        this.onlyElectrified = onlyElectrified;
    }

    /**
     * Loads all Line objects by reading the configured file.
     *
     * @param stations  list of known stations (used to map names to Station instances)
     * @return list of Line instances matching the filter
     * @throws FileNotFoundException if the lines file cannot be read
     */
    public List<Line> loadAll(List<Station> stations) throws FileNotFoundException {
        // Map station name -> Station for quick lookup
        Map<String, Station> map = new HashMap<>();
        for (Station s : stations) {
            map.put(s.getName(), s);
        }

        List<Line> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(linesFile))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(";");
                if (p.length < 3) {
                    continue;  // skip malformed lines
                }
                boolean electrified = p[2].trim().equals("1");
                if (onlyElectrified && !electrified) {
                    continue;
                }
                String fromName = p[0].split("_", 2)[1];
                String toName   = p[1].split("_", 2)[1];
                Station from = map.get(fromName);
                Station to   = map.get(toName);
                if (from != null && to != null) {
                    lines.add(new Line(from, to, electrified));
                }
            }
        }
        return lines;
    }
}
