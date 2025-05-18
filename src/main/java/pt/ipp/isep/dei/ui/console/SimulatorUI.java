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
    private AssignTrainToRouteController assignCtrl;
    private final Scanner scanner = new Scanner(System.in);

    public SimulatorUI(SimulatorController controller,
                       AssignTrainToRouteController assignCtrl) {
        this.controller = Objects.requireNonNull(controller);
        this.assignCtrl = Objects.requireNonNull(assignCtrl);
    }

    public SimulatorUI() {    }


    public void start() {
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
                case "list-stations":
                    assignCtrl.getAvailableStations()
                            .forEach(s -> System.out.println(s.getStationName()));
                    break;
                case "list-cargotypes":
                    assignCtrl.getAvailableCargoTypes()
                            .forEach(ct -> System.out.println(ct));
                    break;
                case "set-route":
                    setRoute();
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

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  start           – start auto simulation");
        System.out.println("  pause           – pause auto simulation");
        System.out.println("  step            – advance one month");
        System.out.println("  status          – show simulation status");
        System.out.println("  list-trains     – list your trains");
        System.out.println("  list-stations   – list available stations");
        System.out.println("  list-cargotypes – list available cargo types");
        System.out.println("  set-route       – assign a route to a train");
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

    public static void main(String[] args) {
        // 1) Repositórios e cenários
        IndustryRepository indRepo  = IndustryRepository.getInstance();
        LocomotiveRepository locoRepo = LocomotiveRepository.getInstance();
        ScenarioRepository scRepo   = new ScenarioRepository();
        LocalDateTime startDate = LocalDateTime.of(2025,1,1,0,0);
        scRepo.addInitialScenario(startDate, new Scenario(startDate));

        // 2) Mapa e simulação
        Map map = new Map("Demo",10,10);
        Date clock = new Date(startDate,20L);
        SimulatorController simCtrl =
                new SimulatorController(map, clock, scRepo, indRepo, locoRepo);

        // 3) Popula estações e indústrias
        map.addStation(new Station("ST1","Central", new Location(2,2),4));
        map.addStation(new Station("ST2","Porto",   new Location(7,1),3));
        map.addStation(new Station("ST3","Indústria", new Location(5,5),2));
        map.addIndustry(indRepo.getIndustryTemplate(4), new Location(3,2));
        map.addIndustry(indRepo.getIndustryTemplate(1), new Location(6,1));
        map.addIndustry(indRepo.getIndustryTemplate(2), new Location(4,5));
        map.addIndustry(indRepo.getIndustryTemplate(3), new Location(5,6));

        // 4) Prepara Player e Trem de exemplo
        Player player = new Player("Alice","20AAArt",50000);
        // adiciona um trem vazio ao player
        Train exampleTrain = new Train("Expresso", locoRepo.getTemplate(0),
                new ArrayList<>(), new ArrayList<>());
        player.addTrain(exampleTrain);

        // 5) Controller de rotas
        AssignTrainToRouteController assignCtrl =
                new AssignTrainToRouteController(player,
                        map.getStations(),
                        List.of(CargoType.values()));

        // 6) Lança UI
        SimulatorUI ui = new SimulatorUI(simCtrl, assignCtrl);
        ui.run();

        // 7) Ao sair
        clock.shutdown();
    }

    /** Menu dispatch entry-point */
    @Override
    public void run() {
        start();
    }
}
