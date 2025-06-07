package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.domain.Map;
import java.util.Scanner;

/**
 * Console UI for the “Create Map” use-case.
 * Guides the user through entering name & dimensions,
 * validating, confirming, and finally saving the Map.
 */
public class CreateMapUI implements Runnable {

    private final CreateMapController ctrl;
    private final Scanner scanner = new Scanner(System.in);

    public CreateMapUI(CreateMapController ctrl) {
        this.ctrl = ctrl;
    }

    @Override
    public void run() {
        create();
    }

    private void create() {
        System.out.print("Enter map name: ");
        String name = scanner.nextLine();

        int width;
        System.out.print("Enter map width: ");
        try {
            width = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Width must be an integer.");
            scanner.nextLine(); // consume invalid token
            return;
        }

        int height;
        System.out.print("Enter map height: ");
        try {
            height = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("Invalid input. Height must be an integer.");
            scanner.nextLine(); // consume invalid token
            return;
        }
        scanner.nextLine();  // consume trailing newline

        if (!ctrl.validateDimensions(width, height)) {
            System.out.println("Error: Dimensions must be positive.");
            return;
        }
        if (!ctrl.validateMapName(name)) {
            System.out.println("Error: Name is empty or duplicate.");
            return;
        }

        System.out.printf("Confirm creation of \"%s\" (%dx%d)? (y/n): ",
                name, width, height);
        String confirm = scanner.nextLine();
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Map creation cancelled.");
            return;
        }

        Map draft = ctrl.createMapDraft(name, width, height);
        ctrl.saveMap(draft);
        System.out.printf("Map created: \"%s\" (%dx%d)%n",
                draft.getName(), draft.getWidth(), draft.getHeight());
    }
}
