package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.UpgradeStationController;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Station;

import java.util.Scanner;

public class UpgradeStationUI implements Runnable {

    private final Station station;
    private final Player player;

    public UpgradeStationUI(Station station, Player player) {
        this.station = station;
        this.player = player;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        UpgradeStationController controller = new UpgradeStationController(station, player);

        System.out.println("\n--- Upgrade Station ---");

        System.out.print("Enter the type of building to install (e.g., TELEPHONE, TELEGRAPH): ");
        String type = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the cost of the upgrade: ");
        double cost;
        try {
            cost = Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid cost. Operation aborted.");
            return;
        }

        if (!controller.isUpgradeAvailable(type)) {
            System.out.println("This upgrade is not available at the current date.");
            return;
        }

        if (!controller.isCompatible(type)) {
            System.out.println("This upgrade is not compatible with the current station.");
            return;
        }

        if (controller.hasBuilding(type)) {
            System.out.println("This building is already installed.");
            return;
        }

        if (!controller.hasSufficientBudget(cost)) {
            System.out.println("You do not have enough budget to apply this upgrade.");
            return;
        }

        boolean success = controller.applyUpgrade(type, cost);
        if (success) {
            System.out.println("Upgrade applied successfully!");
        } else {
            System.out.println("Failed to apply the upgrade.");
        }
    }
}
