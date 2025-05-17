package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.ListTrainsController;
import pt.ipp.isep.dei.domain.Train;

import java.util.List;

/**
 * User interface for listing trains.
 */
public class ListTrainsUI {
    private ListTrainsController controller;

    public ListTrainsUI(ListTrainsController controller) {
        this.controller = controller;
    }

    /**
     * Requests and displays the player's train list.
     */
    public void requestTrainList() {
        List<Train> trains = controller.requestTrainList();
        displayTrainList(trains);
    }

    /**
     * Displays the trains grouped by locomotive type and sorted by name.
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
