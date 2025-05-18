package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based user interface for the simulator.
 * <p>
 * Provides the main loop for user commands to control the simulation,
 * display status, and interact with map elements.
 */
public class SimulatorUI {
    private final SimulatorController controller;
    private final Scanner scanner = new Scanner(System.in);

    public SimulatorUI(SimulatorController controller) {
        this.controller = controller;
    }

    /**
     * Starts the main command loop for user input.
     */
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
                    controller.step();
                    System.out.println("Manual step executed (advanced by one month).");
                    break;
                case "status":
                    showStatus();
                    break;
                case "help":
                    printHelp();
                    break;
                case "exit":
                    controller.pauseSimulation();
                    exit = true;
                    break;
                default:
                    System.out.println("Unknown command. Type 'help' to see available options.");
            }
        }

        System.out.println("Shutting down simulator. Goodbye!");
        scanner.close();
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("  start  – start automatic simulation (tick every 20s)");
        System.out.println("  pause  – pause automatic simulation");
        System.out.println("  step   – manually advance simulation by one month (one tick)");
        System.out.println("  status – display current status of industries, stations, and trains");
        System.out.println("  help   – show this help");
        System.out.println("  exit   – exit the simulator");
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
            String stockInfo = String.format("%s: %.2f",
                    stock.getType(), stock.getQuantity());
            System.out.printf(
                    "[%d] %s | Stock: %s | Prod: %.2f | Dem: %.2f | Locat.: %s%n",
                    i,
                    ind.getIndustryName(),
                    stockInfo,
                    ind.getProduction(),
                    ind.getDemand(),
                    ind.getLocation()
            );
        }

        System.out.println("\n-- Stations and Cargoes --");
        for (Station s : controller.getStations()) {
            System.out.printf("%s @ %s -> %s%n",
                    s.getStationName(),
                    s.getLocation(),
                    s.getCargoes());
        }

        System.out.println("\n-- Locomotives on Map --");
        for (Locomotive l : controller.getMapLocomotives()) {
            System.out.println(l);
        }

        System.out.println("\n-- Locomotive Templates --");
        for (Locomotive tpl : controller.getLocomotiveTemplates()) {
            System.out.println(tpl);
        }
        System.out.println();
    }

    /**
     * Entry point for the console application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        // 1) Inicialização de repositórios
        IndustryRepository indRepo  = IndustryRepository.getInstance();
        LocomotiveRepository locoRepo = LocomotiveRepository.getInstance();
        ScenarioRepository scRepo   = new ScenarioRepository();

        // 2) Define data e cenário inicial
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        Scenario initial = new Scenario(startDate);
        scRepo.addInitialScenario(startDate, initial);

        // 3) Monta o mapa e o clock (20 segundos por mês)
        Map map = new Map("Demo", 10, 10);
        Date clock = new Date(startDate, 20L);

        // 4) Constrói o controller
        SimulatorController controller =
                new SimulatorController(map, clock, scRepo, indRepo, locoRepo);

        // 5) Pré-popula o mapa com estações e indústrias
        map.addStation(new Station("ST1", "Central", new Location(2, 2), 4));
        map.addStation(new Station("ST2", "Porto",   new Location(7, 1), 3));
        map.addStation(new Station("ST3", "Indústria", new Location(5, 5), 2));

        map.addIndustry(indRepo.getIndustryTemplate(4), new Location(3, 2));
        map.addIndustry(indRepo.getIndustryTemplate(1), new Location(6, 1));
        map.addIndustry(indRepo.getIndustryTemplate(2), new Location(4, 5));
        map.addIndustry(indRepo.getIndustryTemplate(3), new Location(5, 6));

        // 6) Adiciona uma locomotiva em rota
        Locomotive loco = locoRepo.getTemplate(0);
        loco.getRoute().add(map.getStations().get(0));
        loco.getRoute().add(map.getStations().get(1));
        loco.getRoute().add(map.getStations().get(2));
        map.addLocomotive(loco);

        // 7) Executa o loop de console
        SimulatorUI ui = new SimulatorUI(controller);
        ui.run();

        // 8) Ao sair, garante desligar o clock
        clock.shutdown();
    }
}
