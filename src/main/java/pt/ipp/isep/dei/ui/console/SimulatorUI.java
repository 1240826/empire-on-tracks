package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.BuildLineController;
import pt.ipp.isep.dei.controller.BuyLocomotiveController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.ui.console.menu.MenuItem;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SimulatorUI implements Runnable {
    private final SimulatorController controller;
    private final Player player;
    private final AssignTrainToRouteController assignCtrl;

    public SimulatorUI(SimulatorController controller, Player player,
                       AssignTrainToRouteController assignCtrl) {
        this.controller = Objects.requireNonNull(controller);
        this.player = player;
        this.assignCtrl = Objects.requireNonNull(assignCtrl);
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
        controller.getLocomotiveTemplates().forEach(System.out::println);

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
        Scanner scanner = new Scanner(System.in);
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

    private void showEditSubmenu() {
        List<MenuItem> editOptions = new ArrayList<>();

        editOptions.add(new MenuItem("UC5 – Build a Station", new BuildStationUI()));
        editOptions.add(new MenuItem("UC6 – Upgrade a Station", new UpgradeStationUI()));
        editOptions.add(new MenuItem("UC7 – List all Stations", new StationListUI()));
        editOptions.add(new MenuItem("UC8 – Build a Railway Line", new BuildLineUI(new BuildLineController(player, Repositories.getInstance().getMapRepository().getCurrentMap()))));
        editOptions.add(new MenuItem("UC9 – Buy a Locomotive", new BuyLocomotiveUI(new BuyLocomotiveController(player))));
        editOptions.add(new MenuItem("UC10 – Assign a Train to a Route", this::setRoute));
        editOptions.add(new MenuItem("UC11 – List all Trains", this::listTrains));
        editOptions.add(new MenuItem("UC12 – Operate Simulation", this::showStatus));

        int choice;
        do {
            choice = Utils.showAndSelectIndex(editOptions, "== EDIT COMMANDS (UC5–UC12) ==");
            if (choice >= 0 && choice < editOptions.size()) {
                editOptions.get(choice).run();
            }
        } while (choice != -1);
    }

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();

        options.add(new MenuItem("Start automatic simulation", () -> {
            controller.startSimulation();
            System.out.println("Automatic simulation started.");
        }));

        options.add(new MenuItem("Pause automatic simulation", () -> {
            controller.pauseSimulation();
            System.out.println("Simulation paused.");
        }));

        options.add(new MenuItem("Advance one month (manual step)", () -> {
            List<String> evs = controller.stepWithEvents();
            System.out.println("Manual step executed (advanced one month).");
            if (evs.isEmpty()) {
                System.out.println("No events this step.");
            } else {
                System.out.println("Events:");
                evs.forEach(e -> System.out.println("  - " + e));
            }
        }));

        options.add(new MenuItem("Show simulation status", this::showStatus));
        options.add(new MenuItem("Edit simulation (UC5–UC12)", this::showEditSubmenu));
        options.add(new MenuItem("Help", this::printHelp));
        options.add(new MenuItem("Exit", controller::pauseSimulation));

        int choice;
        do {
            choice = Utils.showAndSelectIndex(options, "\n--- SIMULATOR MENU ------------------------");
            if (choice >= 0 && choice < options.size()) {
                options.get(choice).run();
            }
        } while (choice != -1);
    }

    private void printHelp() {
        System.out.println("Available options:");
        System.out.println("  - Start: start auto simulation");
        System.out.println("  - Pause: pause auto simulation");
        System.out.println("  - Step: advance one month manually");
        System.out.println("  - Status: show simulation status");
        System.out.println("  - Edit: open UC5–UC12 menu");
        System.out.println("  - Exit: quit simulation");
    }
}
