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
 * Controller principal que expõe funcionalidades para:
 *  1) Exportar matriz de adjacência
 *  2) Check reachability A→B and save closure
 *  3) Display full network graph (cores + layout espaçado)
 *  4) Find maintenance route
 *
 * O construtor recebe:
 *   • stationsCsvPath (path para CSV de estações)
 *   • linesCsvPath    (path para CSV de linhas)
 *   • onlyElectrified  (true para comboio elétrico; false para comboio a diesel/steam)
 *
 * O tipo de comboio (ELECTRIC ou DIESEL) fica “pré-fixado” a partir de onlyElectrified,
 * de modo a não re-perguntar dentro do menu.
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
            // 1) Carrega estações apenas para validar que o ficheiro existe
            StationRepository stationRepo = new StationRepository(stationsCsvPath);
            stationRepo.loadAll();

            // 2) Carrega linhas no grafo, filtrando por electrificação
            LineRepository lineRepo = new LineRepository(linesCsvPath, onlyElectrified);
            lineRepo.loadAll(graph);
        } catch (Exception e) {
            System.err.println("Erro ao carregar ficheiros: " + e.getMessage());
        }
    }

    /**
     * Abre o menu principal (1 a 4 e 0 para sair).
     * Reutiliza defaultTrainType em vez de perguntar de novo.
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
            sc.nextLine(); // consumir newline

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

    /** Opção 1: exporta a matriz de adjacência para "matrix.txt". */
    private void exportMatrix() {
        graph.exportAdjacencyMatrix();
    }

    /**
     * Opção 2: Verifica conectividade A→B exibindo lista de estações,
     * grava a fechadura transitiva em "closure.txt" e pergunta índices.
     */
    private void checkReachability(Scanner sc) {
        System.out.println("\n--- Connectivity Check (US13) ---");

        // 1) Obter a lista ordenada (D→S→T e depois por número)
        List<Station> stations = graph.getSortedStations();
        int n = stations.size();
        System.out.println("Stations:");
        for (int i = 0; i < n; i++) {
            System.out.printf("%2d) %s%n", i, stations.get(i).getName());
        }

        // 2) Construir matriz booleana inicial r[i][j]
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

        // 3) Warshall (fechadura transitiva)
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (!r[i][k]) continue;
                for (int j = 0; j < n; j++) {
                    r[i][j] = r[i][j] || r[k][j];
                }
            }
        }

        // 4) Determinar largura de coluna com base no maior nome de estação
        int maxNameLen = stations.stream()
                .mapToInt(s -> s.getName().length())
                .max()
                .orElse(4);
        int w = maxNameLen + 2;
        String nameFmt = "%" + w + "s";
        String numFmt  = "%" + w + "d";

        // 5) Gravar em "closure.txt", incluindo nomes nas linhas/colunas
        try (PrintWriter out = new PrintWriter("closure.txt")) {
            // 5.1) Cabeçalho: célula vazia + nomes das estações
            out.printf(nameFmt, "");
            for (Station s : stations) {
                out.printf(nameFmt, s.getName());
            }
            out.println();

            // 5.2) Para cada linha i: nome da estação + valores 0/1
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

        // 6) Perguntar índices de origem e destino
        System.out.print("Origin (index): ");
        int src = sc.nextInt();
        System.out.print("Destination (index): ");
        int dst = sc.nextInt();
        sc.nextLine();

        if (src < 0 || src >= n || dst < 0 || dst >= n) {
            System.out.println("Invalid index.");
            return;
        }

        // 7) Exibir resultado
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
     * Opção 3: Exibe o grafo completo, colorindo arestas electrificadas em azul
     * e não-electrificadas em vermelho, usando layout SpringBox para espalhar.
     */
    private void displayGraph() {
        // 1) Criar MultiGraph (permite arestas paralelas)
        MultiGraph gs = new MultiGraph("Railway");

        // 2) Definir stylesheet (cores e rótulos)
        String styleSheet =
                "node {                                   \n" +
                        "    fill-color: gray;                    \n" +
                        "    size: 20px;                          \n" +
                        "    text-size: 14;                       \n" +
                        "    text-alignment: under;               \n" +
                        "    text-color: black;                   \n" +
                        "}                                        \n" +
                        "edge.electrified {                       \n" +
                        "    fill-color: blue;                    \n" +
                        "    size: 3px;                           \n" +
                        "}                                        \n" +
                        "edge.non {                               \n" +
                        "    fill-color: red;                     \n" +
                        "    size: 3px;                           \n" +
                        "}                                        \n";
        gs.setAttribute("ui.stylesheet", styleSheet);

        // 3) Ativar qualidade e anti-aliasing
        gs.setAttribute("ui.quality");
        gs.setAttribute("ui.antialias");

        // 4) Ajustar parâmetros do SpringBox para espalhar mais os nós
        gs.setAttribute("springbox.idealLength", 200);
        gs.setAttribute("springbox.force", 0.8);
        gs.setAttribute("springbox.quality", 1.0);

        // 5) Adicionar nós e arestas ao grafo
        int edgeCounter = 0;
        for (Line l : graph.getAllLines()) {
            String nameA = l.getA().getName();
            String nameB = l.getB().getName();

            // 5.1) Criar nó A se ainda não existir
            if (gs.getNode(nameA) == null) {
                Node nA = gs.addNode(nameA);
                nA.setAttribute("ui.label", nameA);
            }
            // 5.2) Criar nó B se ainda não existir
            if (gs.getNode(nameB) == null) {
                Node nB = gs.addNode(nameB);
                nB.setAttribute("ui.label", nameB);
            }

            // 5.3) Criar aresta única (mesmo par pode ter várias paralelas)
            String edgeId = nameA + "_" + nameB + "_" + (edgeCounter++);
            Edge e = gs.addEdge(edgeId, nameA, nameB);
            if (l.isElectrified()) {
                e.setAttribute("ui.class", "electrified");
            } else {
                e.setAttribute("ui.class", "non");
            }
        }

        // 6) Exibir grafo e deixar SpringBox rodar (true = layout dinâmico)
        gs.display(true);
    }

    /**
     * Opção 4: Find maintenance route usando apenas o filtro definido em onlyElectrified.
     * Não faz nova pergunta – já sabemos se é só electrificadas ou não.
     */
    private void findMaintenanceRoute() {
        String route = graph.buildMaintenanceRoute(onlyElectrified);
        System.out.println("\n🔧 Maintenance route:");
        System.out.println(route);
    }
}
