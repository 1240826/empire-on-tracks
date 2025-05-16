package pt.ipp.isep.dei.Mdisc.repo;

import pt.ipp.isep.dei.Mdisc.domain.Line;
import pt.ipp.isep.dei.Mdisc.domain.Station;

import java.util.List;

public class MatrixRepository {
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