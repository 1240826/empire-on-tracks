package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Player;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller responsible for handling the buy locomotive use case (US09).
 * It manages locomotive availability and player purchase logic.
 */
public class BuyLocomotiveController {
    private Player player;

    /**
     * Constructs a new BuyLocomotiveController with the specified player.
     *
     * @param player the player performing the operation
     */
    public BuyLocomotiveController(Player player) {
        this.player = player;
    }

    /**
     * Retrieves the list of all locomotives currently available for purchase.
     * The list is fetched from the LocomotiveCatalog.
     *
     * @return a list of available locomotives
     */
    public List<Locomotive> requestAvailableLocomotives() {
        return Repositories.getInstance().getLocomotiveCatalog().getAvailableLocomotives();
    }

    /**
     * Attempts to purchase a locomotive with the specified ID.
     * The method checks if the locomotive exists and if the player has sufficient budget.
     *
     * @param locomotiveID the ID of the locomotive to purchase
     * @return true if the locomotive was successfully purchased; false otherwise
     */
    public boolean selectLocomotive(String locomotiveID) {
        List<Locomotive> available = requestAvailableLocomotives();
        for (Locomotive loco : available) {
            if (loco.getId().equals(locomotiveID)) {
                if (player.hasSufficientBudget(loco.getPrice())) {
                    player.buyLocomotive(loco);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the player associated with this controller.
     *
     * @return the current player
     */
    public Player getPlayer() {
        return player;
    }
}