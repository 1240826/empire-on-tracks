package MdiscCorrigido.domain;

import java.io.PrintWriter;
import java.util.*;

/**
 * Represents the railway graph. Internally maintains:
 *   • stationsMap: maps normalized station names → Station
 *   • allLines:    list of all lines (Line) added
 *
 * Main methods:
 *   • addLine(...)               – adds a line, creating stations as needed
 *   • canTravel(...)             – builds connectivity matrix (filters by electrification) + Warshall
 *   • exportAdjacencyMatrix(...) – generates file "matrix.txt" with the complete adjacency matrix
 *   • buildMaintenanceRoute(...) – pre-checks Eulerian/Semi‐Eulerian status and connectivity
 *   • getSortedStations()        – returns a list of stations ordered D→S→T, then by number
 *   • getAllLines()              – returns an unmodifiable list of all lines
 */
public class RailwayGraph {
    private final Map<String, Station> stationsMap = new HashMap<>();
    private final List<Line> allLines           = new ArrayList<>();

    /**
     * Normalises station tokens by removing spaces or underscores after the prefix.
     * For example: "D_8" → "D8".
     *
     * @param token the raw station token
     * @return the normalised station name
     */
    private String normalize(String token) {
        token = token.trim();
        if (token.length() > 1 && token.charAt(1) == '_') {
            return token.substring(0, 1) + token.substring(2);
        }
        return token;
    }

    /**
     * Adds a line (edge) to the graph. Creates a Station if it does not already exist.
     *
     * @param rawA        origin station token (e.g. "D_8" or "D8")
     * @param rawB        destination station token (e.g. "S_2" or "S2")
     * @param electrified {@code true} if the line is electrified; {@code false} otherwise
     * @param distance    distance in kilometres
     */
    public void addLine(String rawA, String rawB, boolean electrified, double distance) {
        String nameA = normalize(rawA);
        String nameB = normalize(rawB);

        Station a = stationsMap.computeIfAbsent(nameA, Station::new);
        Station b = stationsMap.computeIfAbsent(nameB, Station::new);

        allLines.add(new Line(a, b, electrified, distance));
    }

    /**
     * Checks whether there is a path between {@code rawFrom} and {@code rawTo} for the given train type.
     * Builds a boolean matrix, filters out non‐electrified lines if {@code train == ELECTRIC},
     * applies Warshall’s algorithm, and returns {@code true} if a path exists.
     *
     * @param rawFrom raw name of the origin station
     * @param rawTo   raw name of the destination station
     * @param train   type of train (ELECTRIC or DIESEL)
     * @return {@code true} if a path exists; {@code false} otherwise
     */
    public boolean canTravel(String rawFrom, String rawTo, TrainType train) {
        String fromName = normalize(rawFrom);
        String toName   = normalize(rawTo);

        Station fromStation = stationsMap.get(fromName);
        Station toStation   = stationsMap.get(toName);
        if (fromStation == null || toStation == null) {
            return false;
        }

        // 1) Sort all stations by name to fix indices
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort(Comparator.comparing(Station::getName));

        int n = stations.size();
        // 2) Map Station → index
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }

        // 3) Build boolean matrix r[i][j]
        boolean[][] r = new boolean[n][n];
        for (Line l : allLines) {
            if (train == TrainType.ELECTRIC && !l.isElectrified()) {
                continue;
            }
            int i = index.get(l.getA());
            int j = index.get(l.getB());
            r[i][j] = true;
            r[j][i] = true;
        }
        for (int i = 0; i < n; i++) {
            r[i][i] = true;
        }

        // 4) Warshall’s algorithm (transitive closure)
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (!r[i][k]) continue;
                for (int j = 0; j < n; j++) {
                    r[i][j] = r[i][j] || r[k][j];
                }
            }
        }

        int fromIdx = index.get(fromStation);
        int toIdx   = index.get(toStation);
        return r[fromIdx][toIdx];
    }

    /**
     * Exports the complete adjacency matrix (int[n][n], counting parallel lines)
     * to a file named "matrix.txt" in the current directory.
     */
    public void exportAdjacencyMatrix() {
        // 1) Sort stations by name
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort(Comparator.comparing(Station::getName));

        int n = stations.size();
        // 2) Map Station → index
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }

        // 3) Build count matrix
        int[][] matrix = new int[n][n];
        for (Line l : allLines) {
            int i = index.get(l.getA());
            int j = index.get(l.getB());
            matrix[i][j]++;
            matrix[j][i]++;
        }

        // 4) Determine column widths for formatting
        int maxNameLen = stations.stream()
                .mapToInt(s -> s.getName().length())
                .max()
                .orElse(4);
        int maxValLen = 0;
        for (int[] row : matrix) {
            for (int v : row) {
                maxValLen = Math.max(maxValLen, String.valueOf(v).length());
            }
        }
        int w = Math.max(maxNameLen, maxValLen) + 2;
        String nameFmt = "%" + w + "s";
        String numFmt  = "%" + w + "d";

        // 5) Write to file "matrix.txt"
        try (PrintWriter out = new PrintWriter("matrix.txt")) {
            // 5.1) Header: empty cell + station names
            out.printf(nameFmt, "");
            for (Station s : stations) {
                out.printf(nameFmt, s.getName());
            }
            out.println();

            // 5.2) For each row: station name + counts
            for (int i = 0; i < n; i++) {
                out.printf(nameFmt, stations.get(i).getName());
                for (int j = 0; j < n; j++) {
                    out.printf(numFmt, matrix[i][j]);
                }
                out.println();
            }
            System.out.println("Matrix exported to matrix.txt");
        } catch (Exception e) {
            System.err.println("Error exporting matrix: " + e.getMessage());
        }
    }

    /**
     * Determines whether the filtered graph (only electrified or all lines) is Eulerian,
     * Semi‐Eulerian, or not. The steps are:
     *   1) Build filtered adjacency matrix
     *   2) Remove isolated vertices
     *   3) Apply Warshall’s algorithm on the reduced subgraph to check connectivity
     *   4) Count vertices of odd degree
     *   5) Return an appropriate message
     *
     * @param onlyElectrified {@code true} to include only electrified lines; {@code false} to include all
     * @return a message indicating Eulerian, Semi‐Eulerian, or non‐Eulerian status
     */
    public String buildMaintenanceRoute(boolean onlyElectrified) {
        // 1) Sort stations
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort(Comparator.comparing(Station::getName));
        int n = stations.size();

        // 2) Map Station → index
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }

        // 3) Build filtered adjacency matrix adj[n][n]
        int[][] adj = new int[n][n];
        for (Line l : allLines) {
            if (onlyElectrified && !l.isElectrified()) continue;
            int i = index.get(l.getA());
            int j = index.get(l.getB());
            adj[i][j]++;
            adj[j][i]++;
        }

        // 4) Identify non‐isolated vertices
        List<Integer> keep = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            boolean isolated = true;
            for (int j = 0; j < n; j++) {
                if (adj[i][j] > 0) {
                    isolated = false;
                    break;
                }
            }
            if (!isolated) keep.add(i);
        }
        if (keep.isEmpty()) {
            return "Graph has no edges → maintenance route not possible.";
        }

        // 5) Build reduced submatrix m[u][v]
        int mSize = keep.size();
        int[][] m = new int[mSize][mSize];
        for (int u = 0; u < mSize; u++) {
            for (int v = 0; v < mSize; v++) {
                m[u][v] = adj[keep.get(u)][keep.get(v)];
            }
        }

        // 6) Warshall’s algorithm on the reduced subgraph
        boolean[][] r = new boolean[mSize][mSize];
        for (int i = 0; i < mSize; i++) {
            r[i][i] = true;
            for (int j = 0; j < mSize; j++) {
                if (m[i][j] > 0) r[i][j] = true;
            }
        }
        for (int k = 0; k < mSize; k++) {
            for (int i = 0; i < mSize; i++) {
                if (!r[i][k]) continue;
                for (int j = 0; j < mSize; j++) {
                    r[i][j] = r[i][j] || r[k][j];
                }
            }
        }

        // 7) Verify connectivity of the subgraph
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                if (!r[i][j]) {
                    return "Graph is disconnected → maintenance route not possible.";
                }
            }
        }

        // 8) Count vertices of odd degree
        int oddCount = 0;
        for (int i = 0; i < mSize; i++) {
            int degree = 0;
            for (int j = 0; j < mSize; j++) {
                degree += m[i][j];
            }
            if (degree % 2 != 0) oddCount++;
        }

        // 9) Determine Eulerian/Semi‐Eulerian/Non‐Eulerian
        if (oddCount == 0) {
            return "Graph is Eulerian (odd vertices = 0) → maintenance route possible.";
        } else if (oddCount == 2) {
            return "Graph is Semi‐Eulerian (odd vertices = 2) → maintenance route possible.";
        } else {
            return "Graph is not Eulerian (odd vertices = " + oddCount + ") → maintenance route not possible.";
        }
    }

    /**
     * Returns a list of all stations ordered first by type (D → S → T),
     * and then by their numeric part (substring after the first character).
     *
     * @return a sorted list of stations
     */
    public List<Station> getSortedStations() {
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort((a, b) -> {
            String na = a.getName();
            String nb = b.getName();

            // 1) Compare by type (first character): D=0, S=1, T=2
            char ta = Character.toUpperCase(na.charAt(0));
            char tb = Character.toUpperCase(nb.charAt(0));
            int orderA = (ta == 'D' ? 0 : (ta == 'S' ? 1 : 2));
            int orderB = (tb == 'D' ? 0 : (tb == 'S' ? 1 : 2));
            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }

            // 2) If same type, compare numeric part (substring after the first character)
            String partA = na.substring(1);
            String partB = nb.substring(1);
            try {
                return Integer.compare(Integer.parseInt(partA), Integer.parseInt(partB));
            } catch (NumberFormatException e) {
                return partA.compareTo(partB);
            }
        });
        return stations;
    }

    /**
     * Returns an unmodifiable list of all lines (Line) in the graph.
     *
     * @return an unmodifiable list of lines
     */
    public List<Line> getAllLines() {
        return Collections.unmodifiableList(allLines);
    }
}
