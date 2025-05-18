package pt.ipp.isep.dei.Mdisc.controller;

import pt.ipp.isep.dei.Mdisc.domain.Station;
import pt.ipp.isep.dei.Mdisc.domain.Line;
import pt.ipp.isep.dei.Mdisc.repo.StationRepository;
import pt.ipp.isep.dei.Mdisc.repo.LineRepository;
import pt.ipp.isep.dei.Mdisc.repo.MatrixRepository;
import pt.ipp.isep.dei.Mdisc.ui.MenuUI;
import pt.ipp.isep.dei.Mdisc.ui.GraphUI;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application controller handling core computations for US13 and US14.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Load stations and lines (with optional electrification filter).</li>
 *   <li>Export adjacency matrix (counts of parallel edges) to text file using primitive loops.</li>
 *   <li>Compute transitive closure (reachability) via triple nested loops, save result.</li>
 *   <li>Determine Eulerian feasibility (connectedness and odd‐degree count).</li>
 * </ul>
 */
public class AppController {
    private final StationRepository stationRepo;
    private final String linesFile;
    private final MatrixRepository matrixRepo = new MatrixRepository();
    private boolean onlyElectrified;

    /**
     * @param stationsFile    CSV path for station definitions
     * @param linesFile       CSV path for line definitions
     * @param onlyElectrified if true, include only electrified lines in computations
     */
    public AppController(String stationsFile, String linesFile, boolean onlyElectrified) {
        this.stationRepo     = new StationRepository(stationsFile);
        this.linesFile       = linesFile;
        this.onlyElectrified = onlyElectrified;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            MenuUI menu = new MenuUI(onlyElectrified);
            choice = menu.showMenu();
            LineRepository lineRepo = new LineRepository(linesFile, onlyElectrified);

            switch (choice) {
                case 1:
                    exportMatrix(lineRepo);
                    break;
                case 2:
                    checkReachabilityAndSave(sc, lineRepo);
                    break;
                case 3:
                    showFullGraph(lineRepo);
                    break;
                case 4:
                    checkMaintenanceRoute(lineRepo);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        } while (choice != 0);
        sc.close();
    }

    /**
     * US13 Core: Exports integer adjacency matrix to "matrix.txt".
     * <p>
     * Algorithm:
     * <ol>
     *   <li>Load stations list and lines filtered by electrification.</li>
     *   <li>Invoke {@link MatrixRepository#buildAdjacency} to build N×N matrix m.</li>
     *   <li>Determine column width w based on max station name length and digit count.</li>
     *   <li>Print header row with station identifiers.</li>
     *   <li>For each row i, print station i then m[i][j] for j=0..N-1.</li>
     *   <li>All loops are primitive; formatting done with String.format.</li>
     * </ol>
     */
    private void exportMatrix(LineRepository lineRepo) {
        try {
            List<Station> stations = stationRepo.loadAll();
            List<Line> lines       = lineRepo.loadAll(stations);
            int[][] m = matrixRepo.buildAdjacency(stations, lines);
            int n = stations.size();

            int maxName = stations.stream()
                    .mapToInt(s -> s.toString().length())
                    .max().orElse(4);
            int maxVal  = 0;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    maxVal = Math.max(maxVal, String.valueOf(m[i][j]).length());
            int w = Math.max(maxName, maxVal) + 2;
            String nameFmt = "%" + w + "s";
            String numFmt  = "%" + w + "d";

            try (PrintWriter out = new PrintWriter("matrix.txt")) {
                out.printf(nameFmt, "");
                for (Station s : stations) out.printf(nameFmt, s.toString());
                out.println();
                for (int i = 0; i < n; i++) {
                    out.printf(nameFmt, stations.get(i).toString());
                    for (int j = 0; j < n; j++)
                        out.printf(numFmt, m[i][j]);
                    out.println();
                }
                System.out.println("Matrix exported to matrix.txt");
            }
        } catch (Exception ex) {
            System.err.println("Error exporting matrix: " + ex.getMessage());
        }
    }

    /**
     * US13 Core: Computes reachability closure and saves to "closure.txt".
     * <p>
     * Algorithm (transitive closure):
     * <ol>
     *   <li>Load stations and filtered lines; build adjacency matrix m.</li>
     *   <li>Initialize boolean matrix r of size N×N:
     *       <ul>
     *         <li>r[i][i] = true</li>
     *         <li>r[i][j] = (m[i][j] &gt; 0)</li>
     *       </ul>
     *   </li>
     *   <li>For k = 0 to N-1:
     *       <ul>
     *         <li>For i = 0 to N-1:
     *             <ul>
     *               <li>If r[i][k] then for j=0..N-1: r[i][j] |= r[k][j]</li>
     *             </ul></li>
     *       </ul>
     *   </li>
     *   <li>Write r matrix as 0/1 grid to "closure.txt" with row/column indices.</li>
     *   <li>Report r[src][dst] to console.</li>
     * </ol>
     * @param sc       user input scanner for selecting src and dst indices
     * @param lineRepo repository to load lines
     */
    private void checkReachabilityAndSave(Scanner sc, LineRepository lineRepo) {
        try {
            List<Station> stations = stationRepo.loadAll();
            List<Line> lines       = lineRepo.loadAll(stations);
            int n = stations.size();

            for (int i = 0; i < n; i++) {
                System.out.printf("%2d) %s%n", i, stations.get(i));
            }
            System.out.print("Origin: ");
            int src = sc.nextInt();
            System.out.print("Destination: ");
            int dst = sc.nextInt();
            sc.nextLine();

            if (src < 0 || src >= n || dst < 0 || dst >= n) {
                System.out.println("Invalid index.");
                return;
            }

            int[][] m = matrixRepo.buildAdjacency(stations, lines);
            boolean[][] r = new boolean[n][n];
            for (int i = 0; i < n; i++) {
                r[i][i] = true;
                for (int j = 0; j < n; j++) {
                    r[i][j] = m[i][j] > 0;
                }
            }

            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    if (r[i][k]) {
                        for (int j = 0; j < n; j++) {
                            r[i][j] |= r[k][j];
                        }
                    }
                }
            }

            try (PrintWriter out = new PrintWriter("closure.txt")) {
                out.printf("%4s", "");
                for (int i = 0; i < n; i++) out.printf("%4d", i);
                out.println();
                for (int i = 0; i < n; i++) {
                    out.printf("%4d", i);
                    for (int j = 0; j < n; j++)
                        out.printf("%4d", r[i][j] ? 1 : 0);
                    out.println();
                }
                System.out.println("Transitive closure saved to closure.txt");
            }

            if (r[src][dst]) {
                System.out.println("A path exists between " + stations.get(src) + " and " + stations.get(dst));
            } else {
                System.out.println("No path exists between " + stations.get(src) + " and " + stations.get(dst));
            }
        } catch (Exception ex) {
            System.err.println("Error computing reachability: " + ex.getMessage());
        }
    }

    private void showFullGraph(LineRepository lineRepo) {
        try {
            List<Station> stations = stationRepo.loadAll();
            List<Line> lines       = lineRepo.loadAll(stations);
            int[][] m  = matrixRepo.buildAdjacency(stations, lines);
            boolean[][] e = matrixRepo.buildElectrification(stations, lines);

            GraphUI ui = new GraphUI("FullGraph");
            ui.addStations(stations);
            ui.addEdges(stations, m, e);
            ui.display(true);

        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }


    /**
     * US14 Precheck: Determines if a maintenance route is possible.
     * <p>
     * Algorithm:
     * <ol>
     *   <li>Load all stations and filtered lines; build adjacency m0.</li>
     *   <li>Remove isolated vertices—keep indices where sum of row>0.</li>
     *   <li>Build reduced adjacency m for kept vertices.</li>
     *   <li>Compute connectivity r via transitive closure (same as above).
     *       If any r[i][j] false, graph disconnected => no route.
     *   </li>
     *   <li>Count vertices with odd degree = count of (sum m[i][*] mod 2 != 0).
     *       If odd==0 or odd==2 => Eulerian trail/circuit exists;
     *       else => no route.
     *   </li>
     * </ol>
     * Uses only primitive loops and arithmetic.
     * @param lineRepo repository to load lines
     */
    private void checkMaintenanceRoute(LineRepository lineRepo) {
        try {
            List<Station> allStations = stationRepo.loadAll();
            List<Line> lines          = lineRepo.loadAll(allStations);

            int[][] adj0 = matrixRepo.buildAdjacency(allStations, lines);
            int n0 = allStations.size();

            List<Integer> keep = new ArrayList<>();
            for (int i = 0; i < n0; i++) {
                boolean isolated = true;
                for (int j = 0; j < n0; j++) {
                    if (adj0[i][j] > 0) {
                        isolated = false;
                        break;
                    }
                }
                if (!isolated) keep.add(i);
            }

            List<Station> stations = new ArrayList<>();
            for (int idx : keep) stations.add(allStations.get(idx));

            int n = stations.size();
            int[][] m = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    m[i][j] = adj0[keep.get(i)][keep.get(j)];

            boolean[][] r = new boolean[n][n];
            for (int i = 0; i < n; i++) {
                r[i][i] = true;
                for (int j = 0; j < n; j++)
                    if (m[i][j] > 0) r[i][j] = true;
            }
            // Warshall
            for (int k = 0; k < n; k++)
                for (int i = 0; i < n; i++)
                    if (r[i][k])
                        for (int j = 0; j < n; j++)
                            r[i][j] |= r[k][j];

            boolean connected = true;
            for (int i = 0; i < n && connected; i++)
                for (int j = 0; j < n; j++)
                    if (!r[i][j]) { connected = false; break; }

            int odd = 0;
            for (int i = 0; i < n; i++) {
                int deg = 0;
                for (int j = 0; j < n; j++)
                    deg += m[i][j];
                if (deg % 2 != 0) odd++;
            }

            if (!connected) {
                System.out.println("Graph is disconnected → maintenance route not possible.");
            } else if (odd == 0 || odd == 2) {
                System.out.printf("Graph is Eulerian (odd vertices=" + odd + ") → route possible.");
            } else {
                System.out.printf("Graph is not Eulerian (odd vertices=" + odd + ") → route not possible.");
            }

        } catch (Exception ex) {
            System.err.println("Error checking maintenance route: " + ex.getMessage());
        }
    }
}
