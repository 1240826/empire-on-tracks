package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;
import pt.ipp.isep.dei.ui.console.*;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlayerMenuUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();

        options.add(new MenuItem("Select a Map & Start Simulation", this::selectMapAndLaunchSimulator));
        options.add(new MenuItem("Switch to Editor Menu", new EditorMenuUI()));

        int selection;
        do {
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
        MapRepository mapRepo = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepo.getAllMaps();

        if (maps.isEmpty()) {
            System.out.println("No maps available. Create one first.");
            return;
        }

        List<String> names = maps.stream()
                .map(Map::getName)
                .toList();

        int index = Utils.showAndSelectIndex(names, "Select a map to simulate:");
        if (index < 0) return;

        Map selectedMap = maps.get(index);
        mapRepo.setCurrentMap(selectedMap);

        // Dependências
        IndustryRepository indRepo = IndustryRepository.getInstance();
        LocomotiveRepository locoRepo = LocomotiveRepository.getInstance();
        ScenarioRepository scenarioRepo = new ScenarioRepository();
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        scenarioRepo.addInitialScenario(start, new Scenario(start));

        Date clock = new Date(start, 20L);
        SimulatorController simCtrl = new SimulatorController(selectedMap, clock, scenarioRepo, indRepo, locoRepo);

        // Criar player temporário
        Player player = new Player("Alice", "20AAArt", 50000);
        AssignTrainToRouteController routeCtrl = new AssignTrainToRouteController(
                player,
                selectedMap.getStations(),
                List.of(CargoType.values())
        );

        new SimulatorUI(simCtrl, player, routeCtrl).run();
        clock.shutdown();
    }


}


