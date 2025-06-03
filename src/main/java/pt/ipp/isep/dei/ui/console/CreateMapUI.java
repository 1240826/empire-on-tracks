package pt.ipp.isep.dei.ui.console;
import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.MapRepository;

import java.util.Scanner;

/**
 * Console-based UI for creating a new map.
 * <p>
 * Guides the user through the process of entering a map name and dimensions,
 * validates the input, and confirms creation.
 * </p>
 */
public class CreateMapUI implements Runnable {
    private CreateMapController createMapController;
    private MapRepository repositoryMap;

    /**
     * Sets the controller to be used for map creation logic.
     *
     * @param createMapController the CreateMapController instance to inject
     */
    public void setCreateMapController(CreateMapController createMapController) {
        this.createMapController = createMapController;
    }

    /**
     * Starts the interactive flow for creating a map.
     * <p>
     * Steps:
     * <ol>
     *   <li>Prompts the user for a map name and dimensions.</li>
     *   <li>Validates the dimensions and map name.</li>
     *   <li>Asks the user for confirmation before creation.</li>
     *   <li>Creates the map and displays a success message.</li>
     * </ol>
     * </p>
     */
    public void create() {
        int width;
        int height;
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter map name:");
        String name = sc.nextLine();

        try {
            System.out.println("Enter map width:");
            width = sc.nextInt();

            System.out.println("Enter map height:");
            height = sc.nextInt();
        } catch (Exception e) {
            System.out.println("It is not an integer");
            return;
        }

        if (!createMapController.validateDimensions(width, height)) {
            System.out.println("its not positive");
            return;
        }
        if (!createMapController.validateMapName(name)) {
            System.out.println("Error: Invalid or duplicate name.");
            return;
        }
        sc.nextLine();
        System.out.println("confirmation: " + name + " (" + width + "x" + height + ")? (y/n");
        String confirm = sc.nextLine();
        if (confirm.equalsIgnoreCase("n")) {
            System.out.println("Map creation canceled.");
            return;
        }
        createMapController.createMap(name, width, height);
        System.out.println("Map created: " + name + " (" + width + "x" + height + ")");
    }

    /**
     * The menu‐dispatcher entry point
     */
    @Override
    public void run() {
        create();
    }
    public void setRepositoryMap(MapRepository repositoryMap) {
        this.repositoryMap = repositoryMap;
    }

}
