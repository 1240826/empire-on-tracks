// src/main/java/org/example/controller/AppController.java
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

public class AppController {
    private final StationRepository stationRepo;
    private final String linesFile;
    private final MatrixRepository matrixRepo = new MatrixRepository();
    private boolean onlyElectrified;

    public AppController(String stationsFile, String linesFile) {
        this.stationRepo    = new StationRepository(stationsFile);
        this.linesFile      = linesFile;
        this.onlyElectrified = false;
    }

    public void start() {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            MenuUI menu = new MenuUI(onlyElectrified);
            choice = menu.showMenu();
            // cria um repositório de linhas com o flag atualizado
            LineRepository lineRepo = new LineRepository(linesFile, onlyElectrified);

            switch (choice) {
                case 1 :onlyElectrified = !onlyElectrified;
                break;
                case 2 : exportMatrix(lineRepo);
                break;
                case 3 : checkReachabilityAndSave(sc, lineRepo);
                break;
                case 4 : showFullGraph(lineRepo);
                break;
                case 5 : checkMaintenanceRoute(lineRepo);
                break;
                case 0 : System.out.println("Saindo...");
                break;
                default : System.out.println("Opção inválida");
                break;
            }
        } while (choice != 0);
        sc.close();
    }

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
                System.out.println("Matriz exportada para matrix.txt");
            }
        } catch (Exception ex) {
            System.err.println("Erro ao exportar matriz: " + ex.getMessage());
        }
    }

    private void checkReachabilityAndSave(Scanner sc, LineRepository lineRepo) {
        try {
            List<Station> stations = stationRepo.loadAll();
            List<Line> lines       = lineRepo.loadAll(stations);
            int n = stations.size();

            for (int i = 0; i < n; i++) {
                System.out.printf("%2d) %s%n", i, stations.get(i));
            }
            System.out.print("Origem: ");
            int src = sc.nextInt();
            System.out.print("Destino: ");
            int dst = sc.nextInt();
            sc.nextLine();

            if (src < 0 || src >= n || dst < 0 || dst >= n) {
                System.out.println("Índice inválido.");
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
            // Warshall
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
                System.out.println("Fecho transitivo salvo em closure.txt");
            }

            System.out.printf(
                    r[src][dst]
                            ? "Existe caminho de %s para %s.%n"
                            : "Não existe caminho de %s para %s.%n",
                    stations.get(src), stations.get(dst)
            );
        } catch (Exception ex) {
            System.err.println("Erro ao verificar alcance: " + ex.getMessage());
        }
    }

    private void showFullGraph(LineRepository lineRepo) {
        try {
            List<Station> stationes = stationRepo.loadAll();
            List<Line> lines       = lineRepo.loadAll(stationes);
            int[][] m  = matrixRepo.buildAdjacency(stationes, lines);
            boolean[][] e = matrixRepo.buildElectrification(stationes, lines);

            GraphUI ui = new GraphUI("FullGraph");
            ui.addStations(stationes);
            ui.addEdges(stationes, m, e);
            ui.display(true);

        } catch (Exception ex) {
            System.err.println("Erro ao exibir grafo: " + ex.getMessage());
        }
    }

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
                System.out.println("Grafo NÃO é conexo (após remover isolados) → rota impossível.");
            } else if (odd == 0 || odd == 2) {
                System.out.printf("Grafo semi‐Euleriano (ímpares=%d) → rota existe.%n", odd);
            } else {
                System.out.printf("Grafo NÃO semi‐Euleriano (ímpares=%d) → rota impossível.%n", odd);
            }

        } catch (Exception ex) {
            System.err.println("Erro ao checar rota: " + ex.getMessage());
        }
    }
}
