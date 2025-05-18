package pt.ipp.isep.dei.Mdisc.repo;

import pt.ipp.isep.dei.Mdisc.domain.Line;
import pt.ipp.isep.dei.Mdisc.domain.Station;

import java.util.List;

/**
 * Repository building adjacency matrices for a set of stations and lines.
 * <p>
 * Provides methods to construct:
 * <ul>
 *   <li>An integer adjacency matrix counting parallel connections between stations,</li>
 *   <li>A boolean matrix indicating which station pairs are connected by electrified lines.</li>
 * </ul>
 */
public class MatrixRepository {
    /**
     * Builds an undirected adjacency matrix counting direct connections.
     * <p>
     * The returned matrix has size N×N where N = stations.size().
     * Each element matrix[i][j] is the number of lines connecting
     * stations at index i and j in the provided list. The matrix is symmetric
     * and its diagonal entries remain zero (no self-loops).
     * <p>
     * Only primitive loops and array operations are used—no library graph methods.
     *
     * @param stations ordered list of stations defining the matrix indices
     * @param lines    list of lines connecting pairs of stations
     * @return integer adjacency matrix of connection counts
     */
    public int[][] buildAdjacency(List<Station> stations, List<Line> lines) {
        int n = stations.size();
        int[][] matrix = new int[n][n];
        for (Line l : lines) {
            int i = stations.indexOf(l.getFrom());
            int j = stations.indexOf(l.getTo());
            matrix[i][j]++;
            matrix[j][i]++;
        }
        return matrix;
    }

    /**
     * Builds an undirected boolean matrix indicating electrified connections.
     * <p>
     * The returned matrix has size N×N where N = stations.size().
     * matrix[i][j] is true if there is at least one electrified line
     * between stations i and j. The matrix is symmetric and diagonal entries
     * remain false (no self-connections).
     * <p>
     * Only primitive loops and array operations are used—no library graph methods.
     *
     * @param stations ordered list of stations defining the matrix indices
     * @param lines    list of lines connecting pairs of stations
     * @return boolean matrix marking electrified connections
     */
    public boolean[][] buildElectrification(List<Station> stations, List<Line> lines) {
        int n = stations.size();
        boolean[][] matrix = new boolean[n][n];
        for (Line l : lines) {
            if (l.isElectrified()) {
                int i = stations.indexOf(l.getFrom());
                int j = stations.indexOf(l.getTo());
                matrix[i][j] = true;
                matrix[j][i] = true;
            }
        }
        return matrix;
    }
}
