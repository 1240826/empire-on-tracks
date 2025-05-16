package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.BuildLineController;
import pt.ipp.isep.dei.domain.*;

import java.util.List;
import java.util.Scanner;

public class BuildLineUI {
    private final BuildLineController controller;
    private final Scanner scanner = new Scanner(System.in);

    public BuildLineUI(BuildLineController controller) {
        this.controller = controller;
    }

    public void run() {
        List<Station> stations = controller.getAvailableStations();
        if (stations.size() < 2) {
            System.out.println("Not enough stations available.");
            return;
        }

        System.out.println("Available stations:");
        for (int i = 0; i < stations.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, stations.get(i).getStationName());
        }

        System.out.print("Select the first station: ");
        Station s1 = stations.get(scanner.nextInt() - 1);
        System.out.print("Select the second station: ");
        Station s2 = stations.get(scanner.nextInt() - 1);

        System.out.print("Electrified? (true/false): ");
        boolean isElectrified = scanner.nextBoolean();
        System.out.print("Double track? (true/false): ");
        boolean isDouble = scanner.nextBoolean();

        System.out.printf("You selected a line from %s to %s (Electrified: %b, Double: %b)\n",
                s1.getStationName(), s2.getStationName(), isElectrified, isDouble);
        System.out.print("Confirm? (true/false): ");
        if (scanner.nextBoolean()) {
            RailLine line = controller.createRailLine(s1, s2, isElectrified, isDouble);
            if (line != null) {
                System.out.println("Rail line created successfully.");
            } else {
                System.out.println("Failed to create rail line: insufficient budget.");
            }
        } else {
            System.out.println("Operation cancelled.");
        }
    }
}