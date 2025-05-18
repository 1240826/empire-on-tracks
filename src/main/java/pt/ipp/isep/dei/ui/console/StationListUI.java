package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.StationController;
import pt.ipp.isep.dei.domain.*;

import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for listing and viewing station details.
 * <p>
 * Allows the user to see all available stations and select one to display
 * its buildings and cargoes.
 */
public class StationListUI {
    private final StationController controller;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs the UI with the specified controller.
     *
     * @param controller the StationController instance responsible for station operations
     */
    public StationListUI(StationController controller) {
        this.controller = controller;
    }

    /**
     * Runs the interactive station list and detail viewer.
     * <p>
     * Steps:
     * <ol>
     *   <li>Displays all available station names.</li>
     *   <li>Prompts the user to select a station by number.</li>
     *   <li>Displays details of the selected station (buildings and cargoes).</li>
     *   <li>Handles invalid selections and missing stations gracefully.</li>
     * </ol>
     * </p>
     */
    public void run() {
        List<String> stationNames = controller.getAllStations();
        if (stationNames.isEmpty()) {
            System.out.println("No stations available.");
            return;
        }

        System.out.println("Available Stations:");
        for (int i = 0; i < stationNames.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, stationNames.get(i));
        }

        System.out.print("Select a station by number: ");
        int selectedIndex = scanner.nextInt() - 1;
        if (selectedIndex < 0 || selectedIndex >= stationNames.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        String selectedStationName = stationNames.get(selectedIndex);
        Station station = controller.getStationDetails(selectedStationName);

        if (station != null) {
            System.out.println("\nStation: " + station.getStationName());
            System.out.println("Buildings:");
            for (Building b : station.getBuildings()) {
                System.out.println("  - " + b);
            }
            System.out.println("Cargoes:");
            for (Cargo c : station.getCargoes()) {
                System.out.println("  - " + c);
            }
        } else {
            System.out.println("Station not found.");
        }
    }
}