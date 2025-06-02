package MdiscCorrigido.UI;

import MdiscCorrigido.domain.Station;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

import java.util.List;

/**
 * UI para visualizar uma rede ferroviária usando GraphStream.
 *
 * - Nós: cada estação (com rótulo em baixo).
 * - Arestas: coloridas em azul se electrificadas; vermelho se não.
 * - Layout: SpringBox (com parâmetros ajustados para espalhar mais).
 */
public class GraphUI {
    private final MultiGraph graph;

    public GraphUI(String id) {
        // 1) Criar MultiGraph (permite arestas paralelas)
        this.graph = new MultiGraph(id);

        // 2) Definir stylesheet:
        //    - nó cinza, rótulo em baixo
        //    - arestas “electrified” (classe) em azul, “non” em vermelho
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
        graph.setAttribute("ui.stylesheet", styleSheet);

        // 3) Ativar qualidade e anti‐aliasing
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");

        // 4) Ajustar parâmetros do SpringBox para afastar mais os nós
        graph.setAttribute("springbox.idealLength", 200);
        graph.setAttribute("springbox.force", 0.8);
        graph.setAttribute("springbox.quality", 1.0);
    }

    /**
     * Adiciona cada estação à UI como um nó.
     * Se o nó já existir (mesmo ID), não o duplica.
     *
     * @param stations lista de Station
     */
    public void addStations(List<Station> stations) {
        for (Station s : stations) {
            String id = s.getName(); // ex.: "D1", "S2", "T3"
            if (graph.getNode(id) == null) {
                Node n = graph.addNode(id);
                n.setAttribute("ui.label", id);
            }
        }
    }

    /**
     * Adiciona arestas entre estações, de acordo com a matriz adj e a de electrificação.
     * - adj[i][j] > 0 → cria essa quantidade de arestas paralelas.
     * - usa ui.class = "electrified" ou "non" para colorir via CSS.
     *
     * @param stations    lista de Station indexada
     * @param adj         matriz int[n][n] com contagem de ligações
     * @param electrified matriz boolean[n][n] indicando eletrificadas
     */
    public void addEdges(List<Station> stations, int[][] adj, boolean[][] electrified) {
        int n = stations.size();
        int edgeCounter = 0;

        // Percorre apenas j > i para não duplicar (grafo não-direcionado)
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (adj[i][j] > 0) {
                    String from = stations.get(i).getName();
                    String to   = stations.get(j).getName();
                    // Se existirem paralelas, adiciona várias arestas
                    for (int k = 0; k < adj[i][j]; k++) {
                        String edgeId = from + "_" + to + "_" + (edgeCounter++);
                        Edge e = graph.addEdge(edgeId, from, to);
                        // Marca cor: verifica electrified[i][j]
                        if (electrified[i][j]) {
                            e.setAttribute("ui.class", "electrified");
                        } else {
                            e.setAttribute("ui.class", "non");
                        }
                    }
                }
            }
        }
    }

    /**
     * Exibe o grafo em janela Swing. Se autoLayout=true,
     * ativa o layout SpringBox para reorganizar continuamente.
     *
     * @param autoLayout se true, chama v.enableAutoLayout()
     */
    public void display(boolean autoLayout) {
        Viewer v = graph.display();
        if (autoLayout) {
            v.enableAutoLayout();
        }
    }
}
