package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.ListTrainsController;
import pt.ipp.isep.dei.domain.Train;

import java.util.List;

/**
 * Console-based user interface for listing the player's trains.
 * <p>
 * Provides methods to display trains grouped by locomotive type,
 * sorted by name, and shows details for each train.
 */
public class ListTrainsUI {
    private ListTrainsController controller;

    public ListTrainsUI(ListTrainsController controller) {
        this.controller = controller;
    }

    /**
     * Requests the player's train list from the controller and displays it,
     * grouping by locomotive type and sorting by train name.
     */
    public void requestTrainList() {
        List<Train> trains = controller.requestTrainList();
        displayTrainList(trains);
    }

    /**
     * Displays the provided list of trains, grouping them by locomotive type
     * and sorting each group by train name.
     *
     * @param trains the list of trains to display
     */
    public void displayTrainList(List<Train> trains) {
        if (trains == null || trains.isEmpty()) {
            System.out.println("You have no trains yet. Buy a locomotive to create your first train!");
            return;
        }
        String currentType = "";
        for (Train train : trains) {
            if (!train.getLocomotiveType().equals(currentType)) {
                currentType = train.getLocomotiveType();
                System.out.println("\n== " + currentType + " Trains ==");
            }
            System.out.printf("\uD83D\uDE82 %s: Cargoes: %s%n",
                    train.getName(),
                    train.getCurrentCargoes().isEmpty() ? "None" : String.join(", ", train.getCurrentCargoes()));
        }
    }

}
