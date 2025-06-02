package MdiscCorrigido.domain;

import java.io.PrintWriter;
import java.util.*;

/**
 * Representa o grafo ferroviário. Mantém internamente:
 *   • stationsMap: mapeia nomeNormalizado → Station
 *   • allLines:    lista de todas as linhas (Line) adicionadas
 *
 * Métodos principais:
 *   • addLine(...)                 – adiciona uma linha, criando estações conforme necessário
 *   • canTravel(...)               – constrói matriz de conectividade (filtra electrificação) + Warshall
 *   • exportAdjacencyMatrix(...)   – gera ficheiro "matrix.txt" com a matriz de adjacência completa
 *   • buildMaintenanceRoute(...)   – pré‐verifica Eulerianidade/Semi‐Eulerianidade e conectividade
 *   • getSortedStations()          – devolve lista de estações ordenadas D→S→T, depois por número
 *   • getAllLines()                – devolve lista imutável de todas as linhas
 */
public class RailwayGraph {
    private final Map<String, Station> stationsMap = new HashMap<>();
    private final List<Line> allLines           = new ArrayList<>();

    /**
     * Normaliza tokens de estação:
     *   remove espaços e underscore após prefixo, ex.: "D_8" → "D8".
     */
    private String normalize(String token) {
        token = token.trim();
        if (token.length() > 1 && token.charAt(1) == '_') {
            return token.substring(0, 1) + token.substring(2);
        }
        return token;
    }

    /**
     * Adiciona uma linha (aresta) ao grafo. Cria Station se necessário.
     *
     * @param rawA        estação de origem (ex.: "D_8" ou "D8")
     * @param rawB        estação de destino (ex.: "S_2" ou "S2")
     * @param electrified true se for electrificada; false caso contrário
     * @param distance    distância em km
     */
    public void addLine(String rawA, String rawB, boolean electrified, double distance) {
        String nameA = normalize(rawA);
        String nameB = normalize(rawB);

        Station a = stationsMap.computeIfAbsent(nameA, Station::new);
        Station b = stationsMap.computeIfAbsent(nameB, Station::new);

        allLines.add(new Line(a, b, electrified, distance));
    }

    /**
     * Verifica se existe caminho entre rawFrom e rawTo para o comboio train.
     * Constrói matriz booleana, filtra electrificadas se train == ELECTRIC,
     * aplica Warshall e retorna true se houver caminho.
     */
    public boolean canTravel(String rawFrom, String rawTo, TrainType train) {
        String fromName = normalize(rawFrom);
        String toName   = normalize(rawTo);

        Station fromStation = stationsMap.get(fromName);
        Station toStation   = stationsMap.get(toName);
        if (fromStation == null || toStation == null) {
            return false;
        }

        // 1) Ordenar todas as estações por nome para índice fixo
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort(Comparator.comparing(Station::getName));

        int n = stations.size();
        // 2) Mapa Station → índice
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }

        // 3) Construir matriz booleana r[i][j]
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

        // 4) Warshall (fechadura transitiva)
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
     * Exporta a matriz de adjacência completa (int[n][n], contagem de linhas paralelas)
     * para um ficheiro "matrix.txt" no diretório atual.
     */
    public void exportAdjacencyMatrix() {
        // 1) Ordenar estações por nome
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort(Comparator.comparing(Station::getName));

        int n = stations.size();
        // 2) Mapa Station → índice
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }

        // 3) Montar matriz de contagem
        int[][] matrix = new int[n][n];
        for (Line l : allLines) {
            int i = index.get(l.getA());
            int j = index.get(l.getB());
            matrix[i][j]++;
            matrix[j][i]++;
        }

        // 4) Determinar larguras para formatação
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

        // 5) Gravar no ficheiro "matrix.txt"
        try (PrintWriter out = new PrintWriter("matrix.txt")) {
            out.printf(nameFmt, "");
            for (Station s : stations) {
                out.printf(nameFmt, s.getName());
            }
            out.println();

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
     * Verifica se o grafo filtrado (só electrificadas ou todas) é Euleriano/Semi‐Euleriano ou não:
     *   1) Monta matriz adj[n][n] filtrada
     *   2) Remove vértices isolados
     *   3) Aplica Warshall no subgrafo reduzido para verificar conectividade
     *   4) Conta vértices de grau ímpar
     *   5) Retorna mensagem adequada
     */
    public String buildMaintenanceRoute(boolean onlyElectrified) {
        // 1) Ordenar estações
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort(Comparator.comparing(Station::getName));
        int n = stations.size();

        // 2) Mapa Station → índice
        Map<Station, Integer> index = new HashMap<>();
        for (int i = 0; i < n; i++) {
            index.put(stations.get(i), i);
        }

        // 3) Construir matriz adj[n][n] filtrada
        int[][] adj = new int[n][n];
        for (Line l : allLines) {
            if (onlyElectrified && !l.isElectrified()) continue;
            int i = index.get(l.getA());
            int j = index.get(l.getB());
            adj[i][j]++;
            adj[j][i]++;
        }

        // 4) Encontrar vértices não isolados
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

        // 5) Construir submatriz reduzida m[u][v]
        int mSize = keep.size();
        int[][] m = new int[mSize][mSize];
        for (int u = 0; u < mSize; u++) {
            for (int v = 0; v < mSize; v++) {
                m[u][v] = adj[keep.get(u)][keep.get(v)];
            }
        }

        // 6) Warshall no subgrafo reduzido
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

        // 7) Verificar conectividade do subgrafo
        for (int i = 0; i < mSize; i++) {
            for (int j = 0; j < mSize; j++) {
                if (!r[i][j]) {
                    return "Graph is disconnected → maintenance route not possible.";
                }
            }
        }

        // 8) Contar vértices de grau ímpar
        int oddCount = 0;
        for (int i = 0; i < mSize; i++) {
            int degree = 0;
            for (int j = 0; j < mSize; j++) {
                degree += m[i][j];
            }
            if (degree % 2 != 0) oddCount++;
        }

        // 9) Decidir Euleriano/Semi‐Euleriano/Não
        if (oddCount == 0) {
            return "Graph is Eulerian (odd vertices = 0) → maintenance route possible.";
        } else if (oddCount == 2) {
            return "Graph is Semi-Eulerian (odd vertices = 2) → maintenance route possible.";
        } else {
            return "Graph is not Eulerian (odd vertices = " + oddCount + ") → maintenance route not possible.";
        }
    }

    /**
     * Devolve todas as estações ordenadas primeiro por tipo (D → S → T),
     * e depois pela parte numérica (substring após o primeiro carácter).
     */
    public List<Station> getSortedStations() {
        List<Station> stations = new ArrayList<>(stationsMap.values());
        stations.sort((a, b) -> {
            String na = a.getName();
            String nb = b.getName();

            // 1) Comparar pelo tipo (primeiro carácter): D=0, S=1, T=2
            char ta = Character.toUpperCase(na.charAt(0));
            char tb = Character.toUpperCase(nb.charAt(0));
            int orderA = (ta == 'D' ? 0 : (ta == 'S' ? 1 : 2));
            int orderB = (tb == 'D' ? 0 : (tb == 'S' ? 1 : 2));
            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }

            // 2) Se forem do mesmo tipo, comparar a parte numérica (substring após o primeiro carácter)
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
     * Retorna a lista de linhas (Line) do grafo, para uso no controller.
     */
    public List<Line> getAllLines() {
        return Collections.unmodifiableList(allLines);
    }
}
