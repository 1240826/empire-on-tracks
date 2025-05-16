package pt.ipp.isep.dei.Mdisc.ui;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import pt.ipp.isep.dei.Mdisc.domain.Station;

import java.util.List;

public class GraphUI {
    private final Graph graph;

    public GraphUI(String id) {
        graph = new SingleGraph(id);
        graph.setAttribute("ui.stylesheet",
                "node {fill-color:orange; text-size:14;} edge {text-mode:hidden;}"
        );
    }

    public void addStations(List<Station> stations) {
        for (Station s : stations) {
            Node n = graph.addNode(s.getName());
            n.setAttribute("ui.label", s.toString());
        }
    }

    public void addEdges(List<Station> stations, int[][] adj, boolean[][] electrified) {
        int n = stations.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j] > 0) {
                    String id = stations.get(i).getName() + "-" + stations.get(j).getName();
                    Edge e = graph.addEdge(id, stations.get(i).getName(), stations.get(j).getName());
                    e.setAttribute("ui.style",
                            electrified[i][j]
                                    ? "fill-color:blue;"
                                    : "fill-color:red;"
                    );
                }
            }
        }
    }

    public void display(boolean autoLayout) {
        Viewer v = graph.display();
        if (autoLayout) v.enableAutoLayout();
    }
}