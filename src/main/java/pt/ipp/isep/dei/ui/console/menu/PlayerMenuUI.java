// PlayerMenuUI.java
package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.Time;
import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Scenario;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;
import pt.ipp.isep.dei.repository.ScenarioRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.ui.console.SimulatorUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMenuUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();

        options.add(new MenuItem("Select a Map & Start Simulation", this::selectMapAndLaunchSimulator));
        options.add(new MenuItem("Switch to Editor Menu", new EditorMenuUI()));

        int selection;
        do {
            // Volta ao método original que aceita List<MenuItem>
            selection = Utils.showAndSelectIndex(
                    options,
                    "\n--- PLAYER MENU ------------------------"
            );
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }

    private void selectMapAndLaunchSimulator() {
        // 1. Escolhe o mapa
        MapRepository mapRepo = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepo.getAllMaps();
        if (maps.isEmpty()) {
            System.out.println("No maps available. Create one first.");
            return;
        }
        int index = Utils.showAndSelectIndex(
                maps.stream().map(Map::getName).collect(Collectors.toList()),
                "Select a map to simulate:"
        );
        if (index < 0) return;

        Map selectedMap = maps.get(index);
        mapRepo.setCurrentMap(selectedMap);

        // 2. Dependências
        IndustryRepository indRepo = IndustryRepository.getInstance();
        LocomotiveRepository locoRepo = LocomotiveRepository.getInstance();
        ScenarioRepository scenarioRepo = new ScenarioRepository();

        // 3. Datas de início e fim
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime end   = start.plusMonths(20);

        // 4. Inicializa repositório de cenários
        scenarioRepo.addInitialScenario(start, new Scenario(start));

        // 5. Cria o relógio e o simulador
        Time clock = new Time(start, end);
        SimulatorController simCtrl = new SimulatorController(
                selectedMap, clock, scenarioRepo, indRepo, locoRepo
        );

        // 6. Cria player e controller de atribuição de comboios
        Player player = new Player("Alice", "20AAArt", 50000);
        AssignTrainToRouteController routeCtrl = new AssignTrainToRouteController(
                player,
                selectedMap.getStations(),
                List.of(CargoType.values())
        );

        // 7. Inicia simulação automática a cada 10 segundos
        simCtrl.startSimulation(10L);

        // 8. Lança a UI de simulação
        new SimulatorUI(simCtrl, player, routeCtrl).run();

        // 9. Termina o simulador e liberta recursos
        simCtrl.shutdown();
    }
}
