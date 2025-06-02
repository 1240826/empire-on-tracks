package MdiscCorrigido.Utils;

import MdiscCorrigido.domain.RailwayGraph;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Utility class for reading a railway graph from a CSV file.
 * <p>
 * Expected CSV format (one line per railway connection):
 * StationA;StationB;Electrified;Distance
 * Where:
 *   - StationA and StationB are the names (e.g., "D8", "S2", "T1")
 *   - Electrified is "1" for true, anything else for false
 *   - Distance is a number (double) representing the length in km
 */
public class CsvReader {

    /**
     * Loads a RailwayGraph from a CSV file located at the given path.
     *
     * @param path The path to the CSV file.
     * @return A populated RailwayGraph object based on the file contents.
     */
    public static RailwayGraph loadGraph(String path) {
        RailwayGraph graph = new RailwayGraph();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 4) {
                    // Linha malformada, ignora
                    continue;
                }
                String rawA = parts[0].trim();
                String rawB = parts[1].trim();
                boolean electrified = parts[2].trim().equals("1");
                double distance;
                try {
                    distance = Double.parseDouble(parts[3].trim());
                } catch (NumberFormatException e) {
                    // Se a distância não for numérica, ignora esta linha
                    continue;
                }
                graph.addLine(rawA, rawB, electrified, distance);
            }
        } catch (Exception e) {
            System.out.println("❌ Error reading CSV: " + e.getMessage());
        }
        return graph;
    }
}
