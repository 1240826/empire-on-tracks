// File: pt/ipp/isep/dei/ui/console/SimulatorUI.java
package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.AssignTrainToRouteController.StationCargo;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SimulatorUI implements Runnable {
    private SimulatorController controller;
    private final AssignTrainToRouteController assignCtrl;
    private final Scanner scanner = new Scanner(System.in);

    public SimulatorUI(SimulatorController controller,
                       AssignTrainToRouteController assignCtrl) {
        this.controller = Objects.requireNonNull(controller);
        this.assignCtrl = Objects.requireNonNull(assignCtrl);
    }


    private void printEditCommands() {
        System.out.println("== Edit Commands (UC5 to UC12) ==");
        System.out.println("  UC5  – Build a Station");
        System.out.println("  UC6  – Upgrade a Station");
        System.out.println("  UC7  – List all Stations");
        System.out.println("  UC8  – Build a Railway Line");
        System.out.println("  UC9  – Buy a Locomotive");
        System.out.println("  UC10 – Assign a Train to a Route");
        System.out.println("  UC11 – List all Trains");
        System.out.println("  UC12 – Operate Simulation");
        System.out.println("Type the corresponding number or use the main menu options.");
    }


    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  start           – start auto simulation");
        System.out.println("  pause           – pause auto simulation");
        System.out.println("  step            – advance one month");
        System.out.println("  status          – show simulation status");
        System.out.println("  help            – show this help");
        System.out.println("  exit            – quit");
    }

    private void showStatus() {
        LocalDateTime now = controller.getCurrentDate();
        System.out.println("\n== SIMULATION STATUS ==");
        System.out.println("Current date: " + now);

        System.out.println("\n-- Industries on Map --");
        List<Industry> inds = controller.getIndustries();
        for (int i = 0; i < inds.size(); i++) {
            Industry ind = inds.get(i);
            Cargo stock = ind.getCargo();
            System.out.printf("[%d] %s | %s: %.2f | Prod: %.2f | Dem: %.2f | Loc: %s%n",
                    i,
                    ind.getIndustryName(),
                    stock.getType(), stock.getQuantity(),
                    ind.getProduction(),
                    ind.getDemand(),
                    ind.getLocation()
            );
        }

        System.out.println("\n-- Stations and Cargoes --");
        controller.getStations().forEach(s -> {
            String line = s.getCargoes().stream()
                    .filter(c -> c.getQuantity() > 0)
                    .map(c -> String.format("%s: %.2f", c.getType(), c.getQuantity()))
                    .collect(Collectors.joining(", "));
            System.out.printf("%s @ %s -> [%s]%n",
                    s.getStationName(),
                    s.getLocation(),
                    line.isEmpty() ? "vazio" : line);
        });

        System.out.println("\n-- Trains --");
        assignCtrl.getOwnedTrains().forEach(t ->
                System.out.printf("[%s] loco=%s route=%s load=%s%n",
                        t.getName(),
                        t.getLocomotiveType(),
                        t.getRoute() != null ? t.getRoute().toString() : "none",
                        t.getCurrentCargoes()
                )
        );

        System.out.println("\n-- Locomotive Templates --");
        controller.getLocomotiveTemplates()
                .forEach(System.out::println);

        System.out.println();
    }

    private void listTrains() {
        List<Train> trains = assignCtrl.getOwnedTrains();
        if (trains.isEmpty()) {
            System.out.println("No trains owned.");
        } else {
            for (int i = 0; i < trains.size(); i++) {
                Train t = trains.get(i);
                System.out.printf("[%d] %s (route: %s)%n",
                        i,
                        t.getName(),
                        t.getRoute() != null ? t.getRoute().toString() : "none"
                );
            }
        }
    }

    private void setRoute() {
        System.out.print("Train index: ");
        int idx = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter route (e.g. Central, COAL, Porto, STEEL): ");
        String input = scanner.nextLine();
        Route route = assignCtrl.parseRouteFromInput(input);
        if (route == null) {
            System.out.println("Invalid route format!");
            return;
        }
        if (assignCtrl.assignRouteToTrain(idx, route)) {
            System.out.println("Route assigned successfully.");
        } else {
            System.out.println("Failed to assign route.");
        }
    }

    @Override
    public void run() {
        System.out.println("=== Railway Simulator ===");
        printHelp();

        boolean exit = false;
        while (!exit) {
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "start":
                    controller.startSimulation();
                    System.out.println("Automatic simulation started.");
                    break;
                case "pause":
                    controller.pauseSimulation();
                    System.out.println("Automatic simulation paused.");
                    break;
                case "step":
                    List<String> evs = controller.stepWithEvents();
                    System.out.println("Manual step executed (advanced one month).");
                    if (evs.isEmpty()) {
                        System.out.println("No events this step.");
                    } else {
                        System.out.println("Events:");
                        evs.forEach(e -> System.out.println("  - " + e));
                    }
                    break;
                case "status":
                    showStatus();
                    break;
                case "list-trains":
                    listTrains();
                    break;
                case "set-route":
                    setRoute();
                    break;
                case "edit":
                    printEditCommands();
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    controller.pauseSimulation();
                    exit = true;
                    break;
                default:
                    System.out.println("Unknown command. Type 'help'.");
            }
        }
        System.out.println("Goodbye!");
        scanner.close();
    }



    /** Menu dispatch entry-point */

}
