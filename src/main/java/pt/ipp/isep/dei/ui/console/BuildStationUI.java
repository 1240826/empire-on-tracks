package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.BuildStationController;
import pt.ipp.isep.dei.domain.Map;
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

        System.out.print("Enter station name: ");
        String name = scanner.nextLine();

        System.out.print("Enter X coordinate: ");
        int x = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter Y coordinate: ");
        int y = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose station type:");
        StationType[] types = StationType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d - %s%n", i + 1, types[i]);
        }

        int selected = Integer.parseInt(scanner.nextLine()) - 1;
        if (selected < 0 || selected >= types.length) {
            System.out.println("Invalid selection.");
            return;
        }

        boolean result = controller.buildStation(name, x, y, types[selected]);
        System.out.println(result ? "Station built successfully!" : "Error: Failed to build station (area may be occupied).");
    }
}
