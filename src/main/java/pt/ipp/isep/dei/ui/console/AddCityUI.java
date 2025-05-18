package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddCityController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the "Add City" use case.
 *
 * Guides the editor through creating a new City with its center and house blocks,
 * supporting both automatic and manual block assignment methods.
 */
public class AddCityUI {
    private final AddCityController controller = new AddCityController();
    private final Scanner sc = new Scanner(System.in);

    /**
     * Starts the interactive "Add City" flow.
     * <p>
     * Steps:
     * <ol>
     *   <li>Initialize the controller.</n     *   <li>Collect city name, center coordinates, and house block count.</li>
     *   <li>Choose assignment method (automatic/manual).</li>
     *   <li>If manual, collect each block's coordinates.</li>
     *   <li>Attempt block assignment, retrying on errors.</li>
     *   <li>Display a review summary and confirm creation.</li>
     * </ol>
     */
    public void run() {
        System.out.println("=== Add New City ===");

        // Initialize controller state
        controller.startAddCity();

        // Collect basic city data
        System.out.print("Name: ");
        String name = sc.nextLine().trim();

        System.out.print("Position X: ");
        int x = readInt();

        System.out.print("Position Y: ");
        int y = readInt();

        System.out.print("Number of House Blocks: ");
        int hbCount = readInt();

        City city;
        try {
            city = controller.submitCityDetails(name, x, y, hbCount);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Choose assignment method
        String method;
        while (true) {
            System.out.print("Assignment method (automatic/manual): ");
            method = sc.nextLine().trim().toLowerCase();
            if (method.equals("automatic") || method.equals("manual")) {
                break;
            }
            System.out.println("Invalid option. Type 'automatic' or 'manual'.");
        }

        // Collect house block positions if manual assignment
        List<Location> positions = new ArrayList<>();
        if (method.equals("manual")) {
            for (int i = 1; i <= hbCount; i++) {
                System.out.printf("Block %d X: ", i);
                int bx = readInt();
                System.out.printf("Block %d Y: ", i);
                int by = readInt();
                positions.add(new Location(bx, by));
            }
            sc.nextLine(); // consume leftover newline
        }

        // Attempt block assignment, with retry on error
        boolean assigned = false;
        while (!assigned) {
            try {
                controller.selectedAssignmentMethod(method, positions);
                assigned = true;
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
                if (method.equals("manual")) {
                    positions.clear();
                    System.out.println("Please re-enter block coordinates:");
                    for (int i = 1; i <= hbCount; i++) {
                        System.out.printf("Block %d X: ", i);
                        int bx = readInt();
                        System.out.printf("Block %d Y: ", i);
                        int by = readInt();
                        positions.add(new Location(bx, by));
                    }
                    sc.nextLine();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // Review and confirmation
        System.out.println("\nReview City:");
        System.out.println("Name: " + city.getName());
        System.out.println("Center: (" + city.getCenter().getX() + ", " + city.getCenter().getY() + ")");
        System.out.println("House Blocks:");
        for (HouseBlock hbk : city.getHouseBlocks()) {
            System.out.println(" - (" + hbk.getPosition().getX() + ", " + hbk.getPosition().getY() + ")");
        }

        System.out.print("\nConfirm creation? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if (confirm.equals("yes")) {
            controller.confirmCityCreation();
            System.out.println("City created successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    /**
     * Reads an integer from the console, re-prompting on invalid input.
     *
     * @return a valid integer entered by the user
     */
    private int readInt() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Please enter again: ");
            }
        }
    }

    /**
     * Entry point: launches the Add City UI.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new AddCityUI().run();
    }
}
