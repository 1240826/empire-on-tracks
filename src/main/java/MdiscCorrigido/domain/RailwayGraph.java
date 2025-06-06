package MdiscCorrigido.domain;

import java.io.PrintWriter;
import java.util.*;

/**
 * Represents the railway graph. Stores:
 *   • stationsMap : normalised station-name → Station
 *   • allLines    : every Line added
 *
 * Main public methods:
 *   • addLine(...)
 *   • exportAdjacencyMatrix()
 *   • buildMaintenanceRoute(...)    – diagnostic only (Eulerian / etc.)
 *   • findEulerRoute(...)           – returns the actual trail/circuit
 *   • getSortedStations(), getAllLines()
 */
public class RailwayGraph {

    /* ------------------------------------------------------------------ */
    /*  Internal data                                                     */
    /* ------------------------------------------------------------------ */
    private final Map<String, Station> stationsMap = new HashMap<>();
    private final List<Line>           allLines    = new ArrayList<>();

    /* ------------------------------------------------------------------ */
    /*  Normalise token ("D_8" → "D8")                                    */
    /* ------------------------------------------------------------------ */
    private String normalize(String token) {
        token = token.trim();
        if (token.length() > 1 && token.charAt(1) == '_')
            return token.substring(0, 1) + token.substring(2);
        return token;
    }

    /* ------------------------------------------------------------------ */
    /*  Add a line (creates stations as needed)                           */
    /* ------------------------------------------------------------------ */
    public void addLine(String rawA, String rawB,
                        boolean electrified, double distance) {
        String aName = normalize(rawA);
        String bName = normalize(rawB);
        Station a = stationsMap.computeIfAbsent(aName, Station::new);
        Station b = stationsMap.computeIfAbsent(bName, Station::new);
        allLines.add(new Line(a, b, electrified, distance));
    }

    /* ------------------------------------------------------------------ */
    /*  Adjacency-matrix export (exactly your original code)              */
    /* ------------------------------------------------------------------ */
    public void exportAdjacencyMatrix() {
        List<Station> st = new ArrayList<>(stationsMap.values());
        st.sort(Comparator.comparing(Station::getName));

        int n = st.size();
        Map<Station,Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(st.get(i), i);

        int[][] mat = new int[n][n];
        for (Line l : allLines) {
            int i = idx.get(l.getA());
            int j = idx.get(l.getB());
            mat[i][j]++;  mat[j][i]++;
        }

        int nameW = st.stream().mapToInt(s -> s.getName().length()).max().orElse(4) + 2;
        int valW  = 0;
        for (int[] row : mat)
            for (int v : row) valW = Math.max(valW, String.valueOf(v).length());
        int w = Math.max(nameW, valW + 2);

        String nameFmt = "%" + w + "s";
        String numFmt  = "%" + w + "d";

        try (PrintWriter out = new PrintWriter("matrix.txt")) {
            out.printf(nameFmt, "");
            for (Station s : st) out.printf(nameFmt, s.getName());
            out.println();
            for (int i = 0; i < n; i++) {
                out.printf(nameFmt, st.get(i).getName());
                for (int j = 0; j < n; j++)
                    out.printf(numFmt, mat[i][j]);
                out.println();
            }
            System.out.println("Matrix exported to matrix.txt");
        } catch (Exception e) {
            System.err.println("Error exporting matrix: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Diagnostic: Eulerian / Semi / Not / Disconnected (original)       */
    /*  Returns TRUE if a maintenance trail is possible, FALSE otherwise  */
    /* ------------------------------------------------------------------ */
    public boolean buildMaintenanceRoute(boolean onlyElec) {
        List<Station> st = new ArrayList<>(stationsMap.values());
        st.sort(Comparator.comparing(Station::getName));
        int n = st.size();

        Map<Station,Integer> idx = new HashMap<>();
        for (int i = 0; i < n; i++) idx.put(st.get(i), i);

        int[][] adj = new int[n][n];
        for (Line l : allLines) {
            if (onlyElec && !l.isElectrified()) continue;
            int i = idx.get(l.getA());
            int j = idx.get(l.getB());
            adj[i][j]++;  adj[j][i]++;
        }

        /* remove isolated vertices */
        List<Integer> keep = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                if (adj[i][j] > 0) { keep.add(i); break; }
        }
        if (keep.isEmpty()) {
            System.out.println("Graph has no edges → maintenance route not possible.");
            return false;
        }

        int m = keep.size();
        int[][] sub = new int[m][m];
        for (int u = 0; u < m; u++)
            for (int v = 0; v < m; v++)
                sub[u][v] = adj[keep.get(u)][keep.get(v)];

        /* Warshall for connectivity on subgraph */
        boolean[][] r = new boolean[m][m];
        for (int i = 0; i < m; i++) {
            r[i][i] = true;
            for (int j = 0; j < m; j++)
                if (sub[i][j] > 0) r[i][j] = true;
        }
        for (int k = 0; k < m; k++)
            for (int i = 0; i < m; i++)
                if (r[i][k])
                    for (int j = 0; j < m; j++)
                        r[i][j] |= r[k][j];

        for (int i = 0; i < m; i++)
            for (int j = 0; j < m; j++)
                if (!r[i][j]) {
                    System.out.println("Graph is disconnected → maintenance route not possible.");
                    return false;
                }

        int odd = 0;
        for (int i = 0; i < m; i++) {
            int deg = 0;
            for (int j = 0; j < m; j++) deg += sub[i][j];
            if (deg % 2 != 0) odd++;
        }

        if (odd == 0) {
            System.out.println("Graph is Eulerian (odd vertices = 0) → maintenance route possible.");
            return true;
        } else if (odd == 2) {
            System.out.println("Graph is Semi-Eulerian (odd vertices = 2) → maintenance route possible.");
            return true;
        } else {
            System.out.println("Graph is not Eulerian (odd vertices = " + odd + ") → maintenance route not possible.");
            return false;
        }
    }

    /* ------------------------------------------------------------------ */
    /*  NEW: Hierholzer trail / circuit                                   */
    /* ------------------------------------------------------------------ */
    public List<Station> findEulerRoute(boolean onlyElec) {

        /* 0) Build mutable adjacency list */
        Map<Station, List<Station>> adj = new HashMap<>();
        int edgeCnt = 0;
        for (Line l : allLines) {
            if (onlyElec && !l.isElectrified()) continue;
            adj.computeIfAbsent(l.getA(), k -> new ArrayList<>()).add(l.getB());
            adj.computeIfAbsent(l.getB(), k -> new ArrayList<>()).add(l.getA());
            edgeCnt++;
        }
        if (edgeCnt == 0) return null;

        /* 1) Connectivity + odd vertices */
        Station start = adj.keySet().iterator().next();
        Set<Station> vis = new HashSet<>();
        dfsSimple(start, adj, vis);
        if (vis.size() != adj.size()) return null;

        List<Station> odd = new ArrayList<>();
        for (Station v : adj.keySet())
            if (adj.get(v).size() % 2 != 0) odd.add(v);
        if (!(odd.size() == 0 || odd.size() == 2)) return null;
        if (odd.size() == 2) start = odd.get(0);

        /* 2) Hierholzer */
        Deque<Station> stack = new ArrayDeque<>();
        List<Station> path  = new ArrayList<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Station v = stack.peek();
            List<Station> list = adj.get(v);
            while (!list.isEmpty() && list.get(0) == null) list.remove(0); // skip markers
            if (list.isEmpty()) {
                path.add(stack.pop());
            } else {
                Station u = list.remove(0);       // take v-u
                adj.get(u).remove(v);             // remove back edge
                stack.push(u);
            }
        }
        Collections.reverse(path);
        return path;
    }

    /* simple DFS for connectivity check */
    private void dfsSimple(Station v, Map<Station,List<Station>> adj,
                           Set<Station> vis) {
        if (!vis.add(v)) return;
        for (Station u : adj.get(v)) dfsSimple(u, adj, vis);
    }

    /* ------------------------------------------------------------------ */
    /*  Accessors                                                         */
    /* ------------------------------------------------------------------ */
    public List<Station> getSortedStations() {
        List<Station> list = new ArrayList<>(stationsMap.values());
        list.sort((a,b) -> {
            char ta = Character.toUpperCase(a.getName().charAt(0));
            char tb = Character.toUpperCase(b.getName().charAt(0));
            int oa = (ta=='D')?0:(ta=='S'?1:2);
            int ob = (tb=='D')?0:(tb=='S'?1:2);
            if (oa!=ob) return oa-ob;
            try {
                return Integer.parseInt(a.getName().substring(1))
                        - Integer.parseInt(b.getName().substring(1));
            } catch(NumberFormatException e) {
                return a.getName().compareTo(b.getName());
            }
        });
        return list;
    }
    public List<Line> getAllLines() {
        return Collections.unmodifiableList(allLines);
    }
}
