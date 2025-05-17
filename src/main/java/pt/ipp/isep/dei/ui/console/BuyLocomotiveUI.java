package pt.ipp.isep.dei.ui.console;

import java.util.List;
import java.util.Scanner;
import pt.ipp.isep.dei.controller.BuyLocomotiveController;
import pt.ipp.isep.dei.domain.Locomotive;

public class BuyLocomotiveUI {
    private BuyLocomotiveController controller;
    private Scanner scanner = new Scanner(System.in);

    public BuyLocomotiveUI(BuyLocomotiveController controller) {
        this.controller = controller;
    }

    public void start() {
        System.out.println("\n=== Buy Locomotive ===");
        List<Locomotive> locomotives = controller.requestAvailableLocomotives();

        if (locomotives.isEmpty()) {
            System.out.println("No locomotives available.");
            return;
        }

        System.out.println("Available locomotives:");
        for (int i = 0; i < locomotives.size(); i++) {
            Locomotive loco = locomotives.get(i);
            System.out.printf("[%d] %s - $%.2f (%s)\n", i + 1, loco.getId(), loco.getPrice(), loco.getType());
        }

        System.out.print("Choose one to buy (by number): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice < 1 || choice > locomotives.size()) {
            System.out.println("Invalid selection.");
            return;
        }

        Locomotive selected = locomotives.get(choice - 1);

        // Prompt for train name
        System.out.print("\uD83D\uDE82 Enter a name for your new train: ");
        String trainName = scanner.nextLine().trim();

        int result = controller.buyLocomotiveAsTrain(selected.getId(), trainName);

        switch (result) {
            case 0:
                System.out.println(" \uD83D\uDE82 Successfully purchased locomotive and created train: " + trainName);
                break;
            case 1:
                System.out.println("Insufficient budget to purchase this locomotive.");
                break;
            case 2:
                System.out.println("You already have a train with that name. Please choose another name.");
                break;
            case 3:
                System.out.println("Locomotive not found (unexpected error).");
                break;
        }
    }
}
