package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.UpgradeStationController;

import java.util.Scanner;

/**
 * Console UI for upgrading a station by installing a building.
 */
public class UpgradeStationUI {

    private final UpgradeStationController controller;

    public UpgradeStationUI(UpgradeStationController controller) {
        this.controller = controller;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Station Upgrade ===");

        System.out.println("Enter the building you want to add:");
        System.out.println("Available options: TELEGRAPH, TELEPHONE, SMALL_CAFE, LARGE_CAFE, CUSTOMS, POST_OFFICE, SMALL_HOTEL, LARGE_HOTEL, SILO, LIQUID_STORAGE");

        String buildingType = scanner.nextLine().trim().toUpperCase();
        double cost = getBuildingCost(buildingType); // adjust this method as needed

        if (!controller.isUpgradeAvailable(buildingType)) {
            System.out.println("This building is not yet available in the current simulation date.");
            return;
        }

        if (!controller.isCompatible(buildingType)) {
            System.out.println("This building type is not compatible with the current station.");
            return;
        }

        if (controller.hasBuilding(buildingType)) {
            System.out.println("This station already has the selected building.");
            return;
        }

        if (!controller.hasSufficientBudget(cost)) {
            System.out.println("You do not have enough budget to install this building.");
            return;
        }

        boolean success = controller.applyUpgrade(buildingType, cost);
        if (success) {
            System.out.println("Building '" + buildingType + "' successfully added to the station.");
        } else {
            System.out.println("Upgrade failed. Please try again.");
        }
    }

    /**
     * Simulates fetching building costs. Replace with actual cost logic if needed.
     */
    private double getBuildingCost(String type) {
        return switch (type.toUpperCase()) {
            case "TELEGRAPH" -> 500.0;
            case "TELEPHONE" -> 1000.0;
            case "SMALL_CAFE" -> 800.0;
            case "LARGE_CAFE" -> 1200.0;
            case "CUSTOMS" -> 900.0;
            case "POST_OFFICE" -> 700.0;
            case "SMALL_HOTEL" -> 1500.0;
            case "LARGE_HOTEL" -> 2000.0;
            case "SILO" -> 1100.0;
            case "LIQUID_STORAGE" -> 1300.0;
            default -> 1000.0; // fallback/default cost
        };
    }
}
