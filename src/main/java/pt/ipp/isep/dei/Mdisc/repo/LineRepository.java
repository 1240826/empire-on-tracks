// src/main/java/org/example/repo/LineRepository.java
package pt.ipp.isep.dei.Mdisc.repo;

import pt.ipp.isep.dei.Mdisc.domain.Line;
import pt.ipp.isep.dei.Mdisc.domain.Station;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class LineRepository {
    private final String linesFile;
    private final boolean onlyElectrified;

    public LineRepository(String linesFile, boolean onlyElectrified) {
        this.linesFile = linesFile;
        this.onlyElectrified = onlyElectrified;
    }

    public List<Line> loadAll(List<Station> stations) throws FileNotFoundException {
        Map<String, Station> map = new HashMap<>();
        for (Station s : stations) {
            map.put(s.getName(), s);
        }
        List<Line> lines = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(linesFile))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(";");
                if (p.length < 3) continue;
                boolean e = p[2].trim().equals("1");
                if (onlyElectrified && !e) continue;
                String fromName = p[0].split("_", 2)[1];
                String toName   = p[1].split("_", 2)[1];
                Station from = map.get(fromName);
                Station to   = map.get(toName);
                if (from != null && to != null) {
                    lines.add(new Line(from, to, e));
                }
            }
        }
        return lines;
    }
}
