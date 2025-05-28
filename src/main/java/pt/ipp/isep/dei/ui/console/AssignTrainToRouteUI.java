package pt.ipp.isep.dei.ui.console;

import java.util.List;
import java.util.Scanner;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.Route;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.CargoMode;

/**
 * Console-based UI for assigning a route to a player's train.
 */
public class AssignTrainToRouteUI implements Runnable {
    private AssignTrainToRouteController controller;
    private final Scanner scanner = new Scanner(System.in);

    public AssignTrainToRouteUI(AssignTrainToRouteController controller) {
        this.controller = controller;
    }

    public AssignTrainToRouteUI() {}

    public void start() {
        List<Train> trains = controller.getOwnedTrains();
        if (trains.isEmpty()) {
            System.out.println("You have no trains to assign a route to.");
            return;
        }
        System.out.println("Owned trains:");
        for (int i = 0; i < trains.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, trains.get(i).getName());
        }
        System.out.print("Choose a train (by number): ");
        int trainChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline
        if (trainChoice < 1 || trainChoice > trains.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        // Show available stations and cargo types
        System.out.println("Available stations:");
        for (Station s : controller.getAvailableStations()) {
            System.out.println(" - " + s.getStationName());
        }
        System.out.println("Available cargo types:");
        for (CargoType c : controller.getAvailableCargoTypes()) {
            System.out.println(" - " + c.name());
        }
        System.out.println("Available cargo modes:");
        for (CargoMode mode : CargoMode.values()) {
            System.out.println(" - " + mode.name());
        }

        System.out.println("\nEnter your assignment in the following format:");
        System.out.println("Station, Cargo, CargoMode, Station, Cargo, CargoMode, ...");
        System.out.println("Example: Lisbon, IRON, FULL, Lisbon, MAIL, FULL, Porto, PASSENGERS, HALF");
        System.out.print("Input: ");
        String input = scanner.nextLine();

        Route route = controller.parseRouteFromInput(input);
        if (route == null) {
            System.out.println("Invalid route input. Please follow the format exactly.");
            return;
        }

        boolean assigned = controller.assignRouteToTrain(trainChoice - 1, route);
        if (assigned) {
            System.out.println("Route assigned successfully!");
        } else {
            System.out.println("Error: could not assign route.");
        }
    }

    @Override
    public void run() {
        start();
    }
}
