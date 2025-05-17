package pt.ipp.isep.dei.ui.console;

import java.util.List;
import java.util.Scanner;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.domain.Route;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.CargoType;

public class AssignTrainToRouteUI {
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

        System.out.println("Enter your assignment (e.g., Station A, Steel, Station B, Iron):");
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
}

