package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.BuildStationController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.StationType;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.Scanner;

public class BuildStationUI implements Runnable {
    private final Scanner scanner = new Scanner(System.in);
    private final BuildStationController controller;

    public BuildStationUI() {
        Map map = Repositories.getInstance().getMapRepository().getCurrentMap();
        this.controller = new BuildStationController(map);
    }

    @Override
    public void run() {
        System.out.println("=== UC5: Build a Station ===");

        // Step 1: Choose station type
        System.out.println("Choose station type:");
        StationType[] types = StationType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d - %s%n", i + 1, types[i]);
        }

        int selected = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (selected < 0 || selected >= types.length) {
            System.out.println("Invalid selection.");
            return;
        }

        // Step 2: Handle anchor/center (only for Station_4x4)
        // Step 2: Handle anchor/center (only for Station_4x4)
        String anchorDirection = "CENTER";
        if (types[selected] == StationType.Station_4x4) {
            System.out.println("Select anchor direction:");
            System.out.println("1 - NE (top-right)");
            System.out.println("2 - SE (bottom-right)");
            System.out.println("3 - SW (bottom-left)");
            System.out.println("4 - NW (top-left)");
            int anchorChoice = Integer.parseInt(scanner.nextLine().trim());
            switch (anchorChoice) {
                case 1 -> anchorDirection = "NE";
                case 2 -> anchorDirection = "SE";
                case 3 -> anchorDirection = "SW";
                case 4 -> anchorDirection = "NW";
                default -> {
                    System.out.println("Invalid anchor selection. Using default CENTER.");
                    anchorDirection = "CENTER";
                }
            }
        }


        // Step 3: Get coordinates
        System.out.print("Enter X coordinate: ");
        int x = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Y coordinate: ");
        int y = Integer.parseInt(scanner.nextLine().trim());

        // Step 4: Suggest name based on closest city
        String name;
        City closestCity = controller.getClosestCity(x, y);
        if (closestCity != null) {
            System.out.println("Closest city detected: " + closestCity.getName());

            Station closestStation = controller.getClosestStationToCity(closestCity);
            if (closestStation != null) {
                System.out.println("Closest existing station to " + closestCity.getName() + ": "
                        + closestStation.getStationName());
            }

            System.out.print("Do you want to name the station '" + closestCity.getName() + " Station'? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                name = closestCity.getName() + " Station";
            } else {
                System.out.print("Enter your desired station name: ");
                name = scanner.nextLine().trim();
            }
        } else {
            System.out.print("No nearby cities found. Enter your desired station name: ");
            name = scanner.nextLine().trim();
        }

        // Step 5: Build station
        boolean result = controller.buildStation(name, x, y, types[selected], anchorDirection);

        System.out.println(result ? "✅ Station built successfully!" : "❌ Error: Failed to build station (area may be occupied or invalid).");
    }
}
