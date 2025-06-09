package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Train;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the purchase of locomotives and creation of trains for a player.
 *
 * Provides methods to display available locomotives and handle the logic of buying a locomotive
 * and assigning it to a new train.
 */

public class BuyLocomotiveController {
    private Player player;

    public BuyLocomotiveController(Player player) {
        this.player = player;
    }

    public List<Locomotive> requestAvailableLocomotives() {
        return Repositories.getInstance().getLocomotiveCatalog().getAvailableLocomotives();
    }

    /**
     * Buys a locomotive and creates a train for the player, given a unique train name.
     * @return 0 if success, 1 if insufficient budget, 2 if duplicate train name, 3 if locomotive not found
     */
    public int buyLocomotiveAsTrain(String locomotiveID, String trainName) {
        // 1. Check for unique train name
        for (Train t : player.getOwnedTrains()) {
            if (t.getName().equalsIgnoreCase(trainName)) {
                return 2; // Train name already exists
            }
        }

        // 2. Find selected locomotive
        List<Locomotive> available = requestAvailableLocomotives();
        Locomotive chosen = null;
        for (Locomotive loco : available) {
            if (loco.getId().equals(locomotiveID)) {
                chosen = loco;
                break;
            }
        }
        if (chosen == null) return 3; // Not found

        // 3. Check budget
        if (!player.hasSufficientBudget(chosen.getPrice())) {
            return 1; // Not enough money
        }

        // 4. Deduct budget and add to ownedLocomotives (optional)
        player.buyLocomotive(chosen);

        // 5. Create and add new train
        Train newTrain = new Train(trainName, chosen, new ArrayList<>(), new ArrayList<>());
        player.addTrain(newTrain);

        return 0; // Success
    }

    public Player getPlayer() {
        return player;
    }
}
