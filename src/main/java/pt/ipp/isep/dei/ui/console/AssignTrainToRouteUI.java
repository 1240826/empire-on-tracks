package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based UI for assigning a route to a player's train,
 * using a step-by-step interactive process.
 *
 * Now suggests only reachable stations (via BFS) after the first stop.
 */
public class AssignTrainToRouteUI implements Runnable {
    private final AssignTrainToRouteController controller;
    private final Scanner scanner = new Scanner(System.in);

    public AssignTrainToRouteUI(AssignTrainToRouteController controller) {
        this.controller = controller;
    }

    public void start() {
        List<Train> trains = controller.getOwnedTrains();
        if (trains.isEmpty()) {
            System.out.println("You have no trains to assign a route to.");
            return;
        }
        // 1. Select Train
        System.out.println("Owned trains:");
        for (int i = 0; i < trains.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, trains.get(i).getName());
        }
        System.out.print("Choose a train (by number): ");
        int trainChoice = readIntInput();
        if (trainChoice < 1 || trainChoice > trains.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        Train selectedTrain = trains.get(trainChoice - 1);

        controller.startNewRoute();

        boolean addMore = true;
        while (addMore) {
            List<Station> selectableStations;

            // ────────────────────────────────────────────────────────────────────
            // If this is the first stop, allow any available station.
            if (controller.getCurrentRoute().isEmpty()) {
                selectableStations = controller.getAvailableStations();
                System.out.println("\nAvailable stations:");
            }
            // Otherwise, perform BFS from the last-station to suggest reachable ones.
            else {
                Station lastStation = controller.getCurrentRoute()
                        .get(controller.getCurrentRoute().size() - 1)
                        .getStation();
                selectableStations = controller.getAllReachableStations(lastStation);
                if (selectableStations.isEmpty()) {
                    System.out.println("\nNo connected stations available from "
                            + lastStation.getStationName() + ". Ending route selection.");
                    break;
                }
                System.out.println("\nConnected stations to " + lastStation.getStationName() + ":");
            }
            // Print the selectable stations for this iteration:
            for (int i = 0; i < selectableStations.size(); i++) {
                System.out.printf("[%d] %s\n", i + 1, selectableStations.get(i).getStationName());
            }
            System.out.print("Select a station (by number): ");
            int stationChoice = readIntInput();
            if (stationChoice < 1 || stationChoice > selectableStations.size()) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }
            Station station = selectableStations.get(stationChoice - 1);

            // 3. Select Cargoes
            List<CargoType> availableCargoes = controller.getAvailableCargoTypesForStation(station);
            if (availableCargoes.isEmpty()) {
                System.out.println("No available cargo types for this station.");
                continue;
            }
            System.out.println("Available cargo types for this station:");
            for (int i = 0; i < availableCargoes.size(); i++) {
                System.out.printf("[%d] %s\n", i + 1, availableCargoes.get(i).name());
            }
            System.out.println("Select one or more cargo types (comma-separated numbers, e.g., 1,2): ");
            String cargoChoices = scanner.nextLine();
            String[] cargoIndexes = cargoChoices.split(",");
            List<CargoType> cargoes = new ArrayList<>();
            for (String idxStr : cargoIndexes) {
                int idx;
                try {
                    idx = Integer.parseInt(idxStr.trim());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid cargo selection. Try again.");
                    cargoes.clear();
                    break;
                }
                if (idx < 1 || idx > availableCargoes.size()) {
                    System.out.println("Invalid cargo selection. Try again.");
                    cargoes.clear();
                    break;
                }
                CargoType cargoType = availableCargoes.get(idx - 1);
                if (!cargoes.contains(cargoType)) {
                    cargoes.add(cargoType);
                }
            }
            if (cargoes.isEmpty()) continue;

            // 4. Select CargoMode
            System.out.println("Available cargo modes:");
            for (int i = 0; i < CargoMode.values().length; i++) {
                System.out.printf("[%d] %s\n", i + 1, CargoMode.values()[i].name());
            }
            System.out.print("Select cargo mode (by number): ");
            int modeChoice = readIntInput();
            if (modeChoice < 1 || modeChoice > CargoMode.values().length) {
                System.out.println("Invalid mode. Try again.");
                continue;
            }
            CargoMode cargoMode = CargoMode.values()[modeChoice - 1];

            // 5. Confirm & Add POR
            System.out.println("Add stop: " + station.getStationName()
                    + " | Cargoes: " + cargoes
                    + " | Mode: " + cargoMode);
            System.out.print("Confirm this stop? (y/n): ");
            String confirm = scanner.nextLine().trim();
            if (!confirm.equalsIgnoreCase("y")) continue;
            controller.addPointOfRoute(station, cargoes, cargoMode);

            // 6. Add more stops?
            System.out.print("Add another stop? (y/n): ");
            String again = scanner.nextLine().trim();
            if (!again.equalsIgnoreCase("y")) addMore = false;
        }

        // 7. Validate and Assign Route
        if (controller.getCurrentRoute().size() < 2) {
            System.out.println("A route must have at least 2 stops!");
            return;
        }
        boolean assigned = controller.validateAndAssignRoute(selectedTrain);
        if (assigned) {
            System.out.println("Route assigned successfully!");
        } else {
            System.out.println("Error: could not assign route.");
        }
    }

    /** Utility method to read an integer from user input safely. */
    private int readIntInput() {
        while (true) {
            String input = scanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }

    @Override
    public void run() {
        start();
    }
}
