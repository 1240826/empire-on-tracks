package pt.ipp.isep.dei.ui.console;
import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.RepositoryMap;

import java.util.Scanner;

public class CreatMapUI {
    private CreateMapController createMapController;
    private RepositoryMap repositoryMap;

    // Setter para injetar o controller
    public void setCreateMapController(CreateMapController createMapController) {
        this.createMapController = createMapController;
    }

    public void create() {
        repositoryMap.saveMap(new Map());
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
        System.out.println("confirmation: " + name + " (" + width + "x" + height + ")? (y/n");
        if (!sc.nextLine().equalsIgnoreCase("n")) {
            return;
        }
        createMapController.createMap(name, width, height);
        System.out.println("Map created: " + name + " (" + width + "x" + height + ")");
    }
}
