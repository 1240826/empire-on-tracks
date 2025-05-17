package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de usuário em modo texto para o simulador.
 */
public class SimulatorUI {
    private final SimulatorController controller;
    private final Scanner scanner = new Scanner(System.in);

    public SimulatorUI(SimulatorController controller) {
        this.controller = controller;
    }

    /**
     * Inicia o loop principal de leitura de comandos.
     */
    public void run() {
        System.out.println("=== Simulador de Transporte ===");
        printHelp();

        boolean exit = false;
        while (!exit) {
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "start":
                    controller.startSimulation();
                    System.out.println("Simulação automática iniciada.");
                    break;
                case "pause":
                    controller.pauseSimulation();
                    System.out.println("Simulação automática pausada.");
                    break;
                case "step":
                    controller.step();
                    System.out.println("Passo manual executado.");
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
                    System.out.println("Comando desconhecido. Digite 'help' para ver opções.");
            }
        }

        System.out.println("Encerrando simulador. Até logo!");
        scanner.close();
    }

    private void printHelp() {
        System.out.println("Comandos disponíveis:");
        System.out.println("  start  – inicia simulação automática (tick a cada 20s)");
        System.out.println("  pause  – pausa simulação automática");
        System.out.println("  step   – avança manualmente um mês (um tick)");
        System.out.println("  status – exibe estado atual de indústrias, estações e trens");
        System.out.println("  help   – mostra esta ajuda");
        System.out.println("  exit   – sai do simulador");
    }

    private void showStatus() {
        LocalDateTime now = controller.getCurrentDate();
        System.out.println("\n== STATUS DA SIMULAÇÃO ==");
        System.out.println("Data atual: " + now);
        System.out.println("\n-- Indústrias no Mapa --");
        List<Industry> inds = controller.getIndustries();
        for (int i = 0; i < inds.size(); i++) {
            Industry ind = inds.get(i);
            System.out.printf("[%d] %s | Prod: %.2f | Dem: %.2f | Local: %s%n",
                    i,
                    ind.getindustryname(),
                    ind.getProduction(),
                    ind.getDemand(),
                    ind.getLocation());
        }

        System.out.println("\n-- Estações e Estoques --");
        for (Station s : controller.getStations()) {
            System.out.printf("%s @ %s -> %s%n",
                    s.getStationName(),
                    s.getLocation(),
                    s.getCargoes());
        }

        System.out.println("\n-- Templates de Locomotivas --");
        for (Locomotive l : controller.getLocomotiveTemplates()) {
            System.out.println(l);
        }
        System.out.println();
    }

    /**
     * Ponto de entrada do aplicativo de console.
     */
    public static void main(String[] args) {
        // Inicialização de repositórios
        IndustryRepository   indRepo  = IndustryRepository.getInstance();
        LocomotiveRepository locoRepo = LocomotiveRepository.getInstance();
        ScenarioRepository   scRepo   = new ScenarioRepository();

        // Define data e cenário inicial
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 0, 0);
        Scenario initial = new Scenario(startDate);
        scRepo.addInitialScenario(startDate, initial);

        // Monta o mapa e o clock
        Map map = new Map("Demo", 10, 10);
        Date clock = new Date(scRepo);

        // Constrói o controller e a UI
        SimulatorController controller =
                new SimulatorController(map, clock, scRepo, indRepo, locoRepo);
        SimulatorUI ui = new SimulatorUI(controller);

        // Setup inicial (opcional): adicionar estação e indústria exemplo
        map.addStation(new Station("ST1", "Central", new Location(2,2), 4));
        map.addIndustry(indRepo.getIndustryTemplate(0),new Location(2,2));

        // Executa o loop de console
        ui.run();
    }
}
