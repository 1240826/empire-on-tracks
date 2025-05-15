package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the system, with a username, password, and budget.
 */
public class Player extends User {

    private double budget;
    private List<Locomotive> ownedLocomotives = new ArrayList<>();


    /**
     * Constructor to create a new Player.
     *
     * @param username the username of the player
     * @param password the password of the player
     * @param budget the initial budget of the player
     */
    public Player(String username, String password, double budget) {
        super(username, password);
        this.budget = budget;
    }

    /**
     * Gets the player's budget.
     *
     * @return the current budget of the player
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Sets a new budget for the player.
     *
     * @param budget the new budget value
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * Validates the player's budget to ensure it is non-negative.
     *
     * @return true if the budget is valid, false otherwise
     */
    public boolean validateBudget() {
        return budget >= 0;
    }

    /**
     * Checks if the player has enough budget to afford a given price.
     *
     * @param price the price to check against the player's current budget
     * @return true if the player's budget is equal to or greater than the price; false otherwise
     */
    public boolean hasSufficientBudget(double price) {
        return budget >= price;
    }

    /**
     * Attempts to buy the given locomotive by deducting its price from the budget
     * and adding it to the list of owned locomotives.
     *
     * @param loco the locomotive to be purchased
     * @return the locomotive if purchase was successful; null if the player had insufficient budget
     */

    public Locomotive buyLocomotive(Locomotive loco) {
        if (hasSufficientBudget(loco.getPrice())) {
            budget -= loco.getPrice();
            ownedLocomotives.add(loco);
            return loco;
        }
        return null;
    }

    /**
     * Retrieves the list of locomotives currently owned by the player.
     *
     * @return a list of owned locomotives
     */
    public List<Locomotive> getOwnedLocomotives() {
        return ownedLocomotives;
    }
}
