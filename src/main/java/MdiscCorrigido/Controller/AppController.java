package MdiscCorrigido.Controller;

import MdiscCorrigido.domain.Line;
import MdiscCorrigido.domain.RailwayGraph;
import MdiscCorrigido.domain.Station;
import MdiscCorrigido.domain.TrainType;
import MdiscCorrigido.repo.LineRepository;
import MdiscCorrigido.repo.StationRepository;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The main controller exposing functionalities to:
 *  1) Export the adjacency matrix
 *  2) Check reachability A→B and save the transitive closure
 *  3) Display the full network graph (colours + spaced layout)
 *  4) Find a maintenance route
 *
 * The constructor requires:
 *   • stationsCsvPath (path to the CSV of stations)
 *   • linesCsvPath    (path to the CSV of lines)
 *   • onlyElectrified (true for electric train; false for diesel/steam train)
 *
 * The train type (ELECTRIC or DIESEL) is determined from onlyElectrified,
 * so that it does not need to be re-asked in the menu.
 */
public class AppController {
    private final RailwayGraph graph;
    private final boolean onlyElectrified;
    private final TrainType defaultTrainType;

    public AppController(String stationsCsvPath,
                         String linesCsvPath,
                         boolean onlyElectrified) {
        this.onlyElectrified   = onlyElectrified;
        this.defaultTrainType  = onlyElectrified ? TrainType.ELECTRIC : TrainType.DIESEL;
        this.graph             = new RailwayGraph();

        try {
            // 1) Load stations purely to validate that the file exists
            StationRepository stationRepo = new StationRepository(stationsCsvPath);
            stationRepo.loadAll();

            // 2) Load lines into the graph, filtering by electrification
            LineRepository lineRepo = new LineRepository(linesCsvPath, onlyElectrified);
            lineRepo.loadAll(graph);
        } catch (Exception e) {
            System.err.println("Error loading files: " + e.getMessage());
        }
    }

    /**
     * Opens the main menu (options 1 to 4, and 0 to exit).
     * Reuses defaultTrainType instead of asking again.
     */
    public void startInteractive() {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n========= MAIN MENU =========");
            System.out.println("| Electrified only: " + (onlyElectrified ? "YES" : "NO") + "         |");
            System.out.println("-----------------------------");
            System.out.println(" 1) Export adjacency matrix to file");
            System.out.println(" 2) Check reachability A→B and save closure");
            System.out.println(" 3) Display full network graph");
            System.out.println(" 4) Find maintenance route");
            System.out.println(" 0) Exit");
            System.out.print("Select an option: ");
            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    exportMatrix();
                    break;
                case 2:
                    checkReachability(sc);
                    break;
                case 3:
                    displayGraph();
                    break;
                case 4:
                    findMaintenanceRoute();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice != 0);

        sc.close();
    }

    /**
     * Option 1: Exports the adjacency matrix to "matrix.txt".
     */
    private void exportMatrix() {
        graph.exportAdjacencyMatrix();
    }

    /**
     * Option 2: Checks connectivity A→B by displaying the list of stations,
     * writes the transitive closure to "closure.txt", and asks for indices.
     *
     * @param sc the Scanner instance for reading user input
     */
    private void checkReachability(Scanner sc) {
        System.out.println("\n--- Connectivity Check (US13) ---");

        // 1) Obtain the ordered list (by D→S→T and then by number)
        List<Station> stations = graph.getSortedStations();
        int n = stations.size();
        System.out.println("Stations:");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d) %s%n", i, stations.get(i).getName());
        }

        // 2) Build the initial boolean matrix r[i][j]
        boolean[][] r = new boolean[n][n];
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }
        for (Line l : graph.getAllLines()) {
            if (defaultTrainType == TrainType.ELECTRIC && !l.isElectrified()) {
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

        // 3) Warshall's algorithm (transitive closure)
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (!r[i][k]) continue;
                for (int j = 0; j < n; j++) {
                    r[i][j] = r[i][j] || r[k][j];
                }
            }
        }

        // 4) Determine column width based on the longest station name
        int maxNameLen = stations.stream()
                .mapToInt(s -> s.getName().length())
                .max()
                .orElse(4);
        int w = maxNameLen + 2;
        String nameFmt = "%" + w + "s";
        String numFmt  = "%" + w + "d";

        // 5) Write to "closure.txt", including names in rows/columns
        try (PrintWriter out = new PrintWriter("closure.txt")) {
            // 5.1) Header: empty cell + station names
            out.printf(nameFmt, "");
            for (Station s : stations) {
                out.printf(nameFmt, s.getName());
            }
            out.println();

            // 5.2) For each row i: station name + 0/1 values
            for (int i = 0; i < n; i++) {
                out.printf(nameFmt, stations.get(i).getName());
                for (int j = 0; j < n; j++) {
                    out.printf(numFmt, r[i][j] ? 1 : 0);
                }
                out.println();
            }
            System.out.println("Transitive closure saved to closure.txt");
        } catch (Exception e) {
            System.err.println("Error writing closure.txt: " + e.getMessage());
        }

        // 6) Ask for origin and destination indices
        System.out.print("Origin (index): ");
        int src = sc.nextInt();
        System.out.print("Destination (index): ");
        int dst = sc.nextInt();
        sc.nextLine();

        if (src < 0 || src >= n || dst < 0 || dst >= n) {
            System.out.println("Invalid index.");
            return;
        }

        // 7) Display the result
        if (r[src][dst]) {
            System.out.printf("✅ Path exists between %s and %s.%n",
                    stations.get(src).getName(),
                    stations.get(dst).getName());
        } else {
            System.out.printf("❌ No path between %s and %s.%n",
                    stations.get(src).getName(),
                    stations.get(dst).getName());
        }
    }

    /**
     * Option 3: Displays the complete graph, colouring electrified edges in blue
     * and non-electrified edges in red, using the SpringBox layout for spacing.
     */
    private void displayGraph() {
        // 1) Create a MultiGraph (allows parallel edges)
        MultiGraph gs = new MultiGraph("Railway");

        // 2) Define the stylesheet (colours and labels)
        String styleSheet =
                "node {                                   \n" +
                        "    fill-color: grey;                  \n" +
                        "    size: 20px;                        \n" +
                        "    text-size: 14;                     \n" +
                        "    text-alignment: under;             \n" +
                        "    text-color: black;                 \n" +
                        "}                                       \n" +
                        "edge.electrified {                      \n" +
                        "    fill-color: blue;                  \n" +
                        "    size: 3px;                         \n" +
                        "}                                       \n" +
                        "edge.non {                              \n" +
                        "    fill-color: red;                   \n" +
                        "    size: 3px;                         \n" +
                        "}                                       \n";
        gs.setAttribute("ui.stylesheet", styleSheet);

        // 3) Enable quality and anti-aliasing
        gs.setAttribute("ui.quality");
        gs.setAttribute("ui.antialias");

        // 4) Adjust SpringBox parameters to spread nodes further
        gs.setAttribute("springbox.idealLength", 200);
        gs.setAttribute("springbox.force", 0.8);
        gs.setAttribute("springbox.quality", 1.0);

        // 5) Add nodes and edges to the graph
        int edgeCounter = 0;
        for (Line l : graph.getAllLines()) {
            String nameA = l.getA().getName();
            String nameB = l.getB().getName();

            // 5.1) Create node A if it does not yet exist
            if (gs.getNode(nameA) == null) {
                Node nA = gs.addNode(nameA);
                nA.setAttribute("ui.label", nameA);
            }
            // 5.2) Create node B if it does not yet exist
            if (gs.getNode(nameB) == null) {
                Node nB = gs.addNode(nameB);
                nB.setAttribute("ui.label", nameB);
            }

            // 5.3) Create a unique edge (same pair may have multiple parallels)
            String edgeId = nameA + "_" + nameB + "_" + (edgeCounter++);
            Edge e = gs.addEdge(edgeId, nameA, nameB);
            if (l.isElectrified()) {
                e.setAttribute("ui.class", "electrified");
            } else {
                e.setAttribute("ui.class", "non");
            }
        }

        // 6) Display the graph and let SpringBox run (true = dynamic layout)
        gs.display(true);
    }

    /**
     * Option 4: Finds a maintenance route using only the filter defined by onlyElectrified.
     * Does not ask again—electrification preference is already known.
     */
    private void findMaintenanceRoute() {
        String route = graph.buildMaintenanceRoute(onlyElectrified);
        System.out.println("\n🔧 Maintenance route:");
        System.out.println(route);
    }
}
