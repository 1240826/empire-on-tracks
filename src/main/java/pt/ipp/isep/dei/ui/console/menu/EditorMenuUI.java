package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.AddCityUI;
import pt.ipp.isep.dei.ui.console.AddIndustryUI;
import pt.ipp.isep.dei.ui.console.CreateMapUI;
import pt.ipp.isep.dei.ui.console.CreateScenarioUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class EditorMenuUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("UC1: Create a Map",
                new CreateMapUI()));
        options.add(new MenuItem("UC2: Add an Industry",
                new AddIndustryUI()));
        options.add(new MenuItem("UC3: Add a City",
                new AddCityUI()));
        options.add(new MenuItem("UC4: Create a Scenario",
                new CreateScenarioUI()));

        // Jump to Player menu
        options.add(new MenuItem("Switch to Player Menu", new PlayerMenuUI()));

        int selection;
        do {
            selection = Utils.showAndSelectIndex(
                    options,
                    "\n--- EDITOR MENU ------------------------"
            );
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }

}
