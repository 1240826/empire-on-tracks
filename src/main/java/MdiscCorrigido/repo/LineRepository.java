package MdiscCorrigido.repo;

import MdiscCorrigido.domain.RailwayGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Repositório responsável por carregar as linhas (edges) num RailwayGraph.
 * Espera um ficheiro CSV onde cada linha tem o formato:
 *   stationA;stationB;electrifiedFlag;distance
 *
 * Em vez de devolver List<Line>, aqui vamos “injetar” diretamente no RailwayGraph.
 */
public class LineRepository {
    private final String linesFile;
    private final boolean onlyElectrified;

    public LineRepository(String linesFile, boolean onlyElectrified) {
        this.linesFile = Objects.requireNonNull(linesFile, "linesFile must not be null");
        this.onlyElectrified = onlyElectrified;
    }

    /**
     * Lê cada linha do CSV e, para cada registo válido, invoca graph.addLine(...).
     * @param graph o RailwayGraph onde as arestas serão criadas
     * @throws FileNotFoundException se não encontrar linesFile
     */
    public void loadAll(RailwayGraph graph) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new File(linesFile))) {
            while (sc.hasNextLine()) {
                String[] p = sc.nextLine().split(";");
                if (p.length < 4) {
                    continue; // ignora linhas malformadas
                }
                // --- extrair dados
                String rawA = p[0].trim();   // ex: "D8" ou "S_8"
                String rawB = p[1].trim();   // ex: "D7" ou "D_7"
                boolean electrified = p[2].trim().equals("1");
                double distance;
                try {
                    distance = Double.parseDouble(p[3].trim());
                } catch (NumberFormatException e) {
                    continue; // ignora se a distância não for número
                }
                // --- se for apenas electrificadas e esta não for, ignora
                if (onlyElectrified && !electrified) {
                    continue;
                }
                // --- adicionar ao grafo
                graph.addLine(rawA, rawB, electrified, distance);
            }
        }
    }
}
