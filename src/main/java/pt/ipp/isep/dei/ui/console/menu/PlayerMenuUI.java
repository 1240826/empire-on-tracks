package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.ui.console.*;
import pt.ipp.isep.dei.ui.console.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class PlayerMenuUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        options.add(new MenuItem("UC5: Build a Station",
                new BuildStationUI()));
        options.add(new MenuItem("UC6: Upgrade a Station",
                new UpgradeStationUI()));
        options.add(new MenuItem("UC7: List all Stations",
                new StationListUI()));
        options.add(new MenuItem("UC8: Build a Railway Line",
                new BuildLineUI()));
        options.add(new MenuItem("UC9: Buy a Locomotive",
                new BuyLocomotiveUI()));
        options.add(new MenuItem("UC10: Assign a Train to a Route",
                new AssignTrainToRouteUI()));
        options.add(new MenuItem("UC11: List all Trains",
                new ListTrainsUI()));
        options.add(new MenuItem("UC12: Operate Simulation",
                new OperateSimulationUI()));

        // Jump back to Editor menu
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

}
