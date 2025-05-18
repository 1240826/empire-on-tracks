package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.MapRepository;

import java.util.Scanner;

/**
 * Console-based UI for creating new Map instances.
 * <p>
 * Prompts the user for map name and dimensions, validates input,
 * and delegates creation to the CreateMapController.
 */
public class CreatMapUI {

    private CreateMapController createMapController;
    private MapRepository repositoryMap;

    /**
     * Injects the controller responsible for map creation logic.
     *
     * @param createMapController the controller to handle validation and creation
     */
    public void setCreateMapController(CreateMapController createMapController) {
        this.createMapController = createMapController;
    }

    /**
     * Runs the interactive creation process:
     * <ol>
     *   <li>Prompts for map name.</li>
     *   <li>Prompts for width and height.</li>
     *   <li>Validates inputs via controller.</li>
     *   <li>Asks for confirmation from the user.</li>
     *   <li>Delegates actual map creation to the controller.</li>
     * </ol>
     */
    public void create() {
        repositoryMap.saveMap(new Map());

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter map name: ");
        String name = sc.nextLine();

        int width, height;
        try {
            System.out.print("Enter map width: ");
            width = sc.nextInt();

            System.out.print("Enter map height: ");
            height = sc.nextInt();
            sc.nextLine(); // consume trailing newline
        } catch (Exception e) {
            System.out.println("Error: Width and height must be integers.");
            return;
        }

        if (!createMapController.validateDimensions(width, height)) {
            System.out.println("Error: Dimensions must be positive.");
            return;
        }
        if (!createMapController.validateMapName(name)) {
            System.out.println("Error: Invalid or duplicate map name.");
            return;
        }

        System.out.printf("Confirm creation of map \"%s\" with size %dx%d? (y/n): ", name, width, height);
        String confirmation = sc.nextLine().trim();
        if (!confirmation.equalsIgnoreCase("y")) {
            System.out.println("Map creation cancelled.");
            return;
        }

        createMapController.createMap(name, width, height);
        System.out.printf("Map created: %s (%dx%d)%n", name, width, height);
    }
}
