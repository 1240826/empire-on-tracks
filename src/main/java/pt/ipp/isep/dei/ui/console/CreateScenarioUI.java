package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.CreateScenarioController;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.CargoType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Console-based user interface for the "Create Scenario" use case.
 * <p>
 * Guides an editor through defining a new scenario.
 * <p>
 * This UI handles user interaction in the following order:
 * <ol>
 *   <li>Collect scenario period ticks (start and end).</li>
 *   <li>Select and add locomotive templates, specifying availability for each.</li>
 *   <li>Select cargo types, assign multiple behaviors for each.</li>
 *   <li>Set the industry production frequency (percentage).</li>
 *   <li>Confirm scenario creation and display the outcome.</li>
 * </ol>
 */
public class CreateScenarioUI implements Runnable {
    private final CreateScenarioController controller = new CreateScenarioController();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Runs the interactive scenario creation flow, prompting the user step-by-step
     * and invoking corresponding controller methods.
     */
    public void start() {
        System.out.println("--- Create Scenario ---");

        // 1) Scenario period
        int periodStart, periodEnd;
        while (true) {
            try {
                System.out.print("Enter scenario period start (int tick): ");
                periodStart = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter scenario period end (int tick): ");
                periodEnd = Integer.parseInt(scanner.nextLine().trim());
                controller.startCreateScenario(LocalDateTime.now(), periodStart, periodEnd);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
                System.out.println("Please enter a valid period where start <= end.\n");
            }
        }

        // 2) Locomotive selection and availability
        List<Locomotive> templates = controller.getAllLocomotiveTemplates();
        System.out.println("Available locomotive templates:");
        for (int i = 0; i < templates.size(); i++) {
            System.out.printf("[%d] %s%n", i, templates.get(i));
        }
        System.out.print("How many locomotive types to add? ");
        int locoCount = Integer.parseInt(scanner.nextLine().trim());
        Set<Integer> chosen = new HashSet<>();
        for (int i = 0; i < locoCount; i++) {
            int idx;
            while (true) {
                System.out.print("Enter template index to add: ");
                idx = Integer.parseInt(scanner.nextLine().trim());
                if (idx < 0 || idx >= templates.size()) {
                    System.out.println("Invalid index. Choose a valid template number.");
                } else if (!chosen.add(idx)) {
                    System.out.println("Template already chosen. Pick a different one.");
                } else {
                    break;
                }
            }
            controller.addLocomotiveByTemplate(idx);
            System.out.printf("Locomotive %d added.%n", idx);
            int availStart, availEnd;
            int locoStart = templates.get(idx).getStartYear();
            System.out.printf("  Note: this locomotive is available from tick %d.%n", locoStart);
            while (true) {
                System.out.print("  Enter availability start tick: ");
                availStart = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("  Enter availability end tick:   ");
                availEnd = Integer.parseInt(scanner.nextLine().trim());
                if (availStart < locoStart) {
                    System.out.printf("Error: availability start (%d) must be >= locomotive start year (%d).%n", availStart, locoStart);
                    continue;
                }
                if (availEnd > periodEnd) {
                    System.out.printf("Error: availability end (%d) must be <= scenario end tick (%d).%n", availEnd, periodEnd);
                    continue;
                }
                if (availStart > availEnd) {
                    System.out.println("Error: start tick must be <= end tick.");
                    continue;
                }
                try {
                    controller.setLocomotiveAvailability(i, availStart, availEnd);
                    System.out.println("Availability set.\n");
                    break;
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

        // 3) Cargo selection and behaviors
        CargoType[] cargoTypes = CargoType.values();
        System.out.println("Available cargo types:");
        for (int i = 0; i < cargoTypes.length; i++) {
            System.out.printf("[%d] %s%n", i, cargoTypes[i]);
        }
        System.out.print("How many cargo types to add? ");
        int cargoCount = Integer.parseInt(scanner.nextLine().trim());
        for (int i = 0; i < cargoCount; i++) {
            System.out.print("Enter cargo type index to add: ");
            int cidx = Integer.parseInt(scanner.nextLine().trim());
            CargoType ct = cargoTypes[cidx];
            controller.addCargoType(ct);
            System.out.printf("Cargo %s added.%n", ct);
            String[] options = {"import", "export", "transform"};
            System.out.print("  How many behaviors to assign? ");
            int behCount = Integer.parseInt(scanner.nextLine().trim());
            Set<String> chosenBehaviors = new HashSet<>();
            for (int b = 0; b < behCount; b++) {
                while (true) {
                    System.out.print("    Enter behavior: ");
                    String behavior = scanner.nextLine().trim().toLowerCase();
                    boolean valid = false;
                    for (String opt : options) {
                        if (opt.equals(behavior)) { valid = true; break; }
                    }
                    if (!valid) {
                        System.out.print("      Invalid. Choose from import/export/transform: ");
                        continue;
                    }
                    if (!chosenBehaviors.add(behavior)) {
                        System.out.println("      Behavior already assigned.");
                        continue;
                    }
                    controller.setCargoBehavior(ct, behavior);
                    break;
                }
            }
            System.out.println();
        }

        // 4) Industry production frequency
        int freq;
        while (true) {
            System.out.print("Enter industry production frequency (0-100): ");
            try {
                freq = Integer.parseInt(scanner.nextLine().trim());
                controller.setIndustryProductionFrequency(freq);
                break;
            } catch (IllegalArgumentException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }

        // 5) Confirmation and persistence
        System.out.print("Confirm scenario creation? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        if ("y".equals(confirm) || "yes".equals(confirm)) {
            boolean ok = controller.confirmScenarioCreation();
            if (ok) {
                System.out.println("Scenario created successfully!");
            } else {
                System.out.println("Failed to create scenario.");
            }
        } else {
            System.out.println("Scenario creation cancelled.");
        }
    }
    /**
     * Entry‐point for Runnable dispatch.
     */
    @Override
    public void run() {
        start();
    }

    /**
     * Entry point for launching this console UI.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new CreateScenarioUI().start();
    }
}
