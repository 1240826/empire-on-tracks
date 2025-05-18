package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.ui.console.*;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class EditorMenuUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();

        // Opções principais
        options.add(new MenuItem("UC1: Create a New Map", this::createNewMap));
        options.add(new MenuItem("Edit Existing Map", this::editExistingMap));
        options.add(new MenuItem("Switch to Player Menu", new PlayerMenuUI()));

        int selection;
        do {
            selection = Utils.showAndSelectIndex(options, "\n--- EDITOR MENU ------------------------");
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }

    private void createNewMap() {
        CreateMapUI ui = new CreateMapUI();
        ui.setRepositoryMap(Repositories.getInstance().getMapRepository());
        ui.setCreateMapController(new CreateMapController());
        ui.run();
    }

    private void editExistingMap() {
        MapRepository repo = Repositories.getInstance().getMapRepository();
        List<String> mapNames = repo.getMapsNames();

        if (mapNames.isEmpty()) {
            System.out.println("No maps available. Please create a map first.");
            return;
        }

        int selected = Utils.showAndSelectIndex(mapNames, "Select a map to edit:");
        if (selected == -1) return;

        String selectedMapName = mapNames.get(selected);
        repo.setCurrentMap(repo.getDetails(selectedMapName));
        System.out.println("Now editing: " + selectedMapName);

        showMapEditSubmenu();
    }

    private void showMapEditSubmenu() {
        List<MenuItem> editOptions = new ArrayList<>();
        AddIndustryUI industryUI = new AddIndustryUI(Repositories.getInstance(), Repositories.getInstance().getMapRepository().getCurrentMap());

        editOptions.add(new MenuItem("UC2: Add an Industry", industryUI));
        editOptions.add(new MenuItem("UC3: Add a City", new AddCityUI()));
        editOptions.add(new MenuItem("UC4: Create a Scenario", new CreateScenarioUI()));
        editOptions.add(new MenuItem("Back to Editor Menu", this));

        int choice;
        do {
            choice = Utils.showAndSelectIndex(editOptions, "--- MAP EDIT MENU ------------------------");
            if (choice >= 0 && choice < editOptions.size()) {
                editOptions.get(choice).run();
            }
        } while (choice != -1);
    }
}
