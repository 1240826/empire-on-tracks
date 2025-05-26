package pt.ipp.isep.dei.Mdisc.ui;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.Mdisc.domain.Station;

import java.util.List;

/**
 * UI for visualizing a railway network using GraphStream.
 */
public class GraphUI {
    private final Graph graph;

    public GraphUI(String id) {
        graph = new MultiGraph(id);
        graph.setAttribute("ui.stylesheet",
                "node {fill-color:orange; text-size:14;} edge {text-mode:hidden;}"
        );
    }

    /**
     * Adds each station to the graph as a node.
     * <p>
     * Node IDs are the station names; each node is labeled with {@code station.toString()}.
     *
     * @param stations list of Station instances to add as nodes
     */
    public void addStations(List<Station> stations) {
        for (Station s : stations) {
            String id = s.getName();
            if (graph.getNode(id) == null) {
                Node n = graph.addNode(id);
                n.setAttribute("ui.label", s.toString());
            }
        }
    }


    /**
     * Adds edges between stations based on adjacency and electrification matrices.
     * <p>
     * For each pair (i, j) where {@code adj[i][j] > 0}, adds an undirected edge
     * with ID "stationI-stationJ". Electrified edges are colored blue;
     * non-electrified edges are colored red.
     *
     * @param stations      ordered list of stations corresponding to matrix indices
     * @param adj           integer adjacency matrix (counts of connections)
     * @param electrified   boolean matrix indicating which connections are electrified
     */
    public void addEdges(List<Station> stations, int[][] adj, boolean[][] electrified) {
        int n = stations.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j] > 0) {
                    String from = stations.get(i).getName();
                    String to = stations.get(j).getName();
                    String id = from + "-" + to;

                    if (graph.getEdge(id) == null) {
                        Edge e = graph.addEdge(id, from, to);
                        if (electrified[i][j]) {
                            e.setAttribute("ui.style", "fill-color:blue;");
                        } else {
                            e.setAttribute("ui.style", "fill-color:red;");
                        }
                    }
                }
            }
        }
    }



    /**
     * Displays the constructed graph in a window.
     */
    public void display(boolean autoLayout) {
        Viewer v = graph.display();
        if (autoLayout) {
            v.enableAutoLayout();
        }
    }
}
