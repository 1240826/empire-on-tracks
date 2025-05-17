package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents a player in the system.
 * A player has a username, password, budget, a list of owned locomotives, and a list of owned trains.
 * <p>
 * Supports core actions for buying locomotives (US9), creating and managing trains (US10, US11).
 */
public class Player extends User {

    /** The current budget available to the player. */
    private double budget;

    /** The list of locomotives purchased and owned by the player. */
    private final List<Locomotive> ownedLocomotives = new ArrayList<>();

    /** The list of trains owned by the player. */
    private final List<Train> ownedTrains = new ArrayList<>();

    /**
     * Constructs a new Player.
     *
     * @param username the username of the player
     * @param password the password of the player
     * @param budget   the initial budget of the player
     */
    public Player(String username, String password, double budget) {
        super(username, password);
        this.budget = budget;
    }

    /**
     * Returns the player's current budget.
     *
     * @return the player's budget
     */
    public double getBudget() {
        return budget;
    }

    /**
     * Sets the player's budget.
     *
     * @param budget the new budget value
     */
    public void setBudget(double budget) {
        this.budget = budget;
    }

    /**
     * Validates that the player's budget is non-negative.
     *
     * @return true if the budget is non-negative, false otherwise
     */
    public boolean validateBudget() {
        return budget >= 0;
    }

    /**
     * Checks if the player has enough budget to afford a given price.
     *
     * @param price the price to check
     * @return true if the player's budget is equal to or greater than the price, false otherwise
     */
    public boolean hasSufficientBudget(double price) {
        return budget >= price;
    }

    /**
     * Attempts to buy the given locomotive by deducting its price from the player's budget
     * and adding it to the player's owned locomotives.
     *
     * @param loco the locomotive to purchase
     * @return the locomotive if the purchase was successful, or null if insufficient budget
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
     * Returns an unmodifiable list of locomotives owned by the player.
     *
     * @return an unmodifiable list of owned locomotives
     */
    public List<Locomotive> getOwnedLocomotives() {
        return Collections.unmodifiableList(ownedLocomotives);
    }

    // --- US11 Support: Trains ---

    /**
     * Adds a train to the player's owned trains.
     * Used when a player buys a locomotive and creates a train (US9/US10).
     *
     * @param train the train to add
     */
    public void addTrain(Train train) {
        ownedTrains.add(train);
    }

    /**
     * Returns an unmodifiable list of all trains owned by the player.
     * Used for listing trains (US11).
     *
     * @return an unmodifiable list of owned trains
     */
    public List<Train> getOwnedTrains() {
        return Collections.unmodifiableList(ownedTrains);
    }

    /**
     * Deducts a specified amount from the player's budget, if possible.
     *
     * @param amount the amount to spend
     * @return true if the amount was successfully deducted, false otherwise
     */
    public boolean spend(double amount) {
        if (hasSufficientBudget(amount)) {
            budget -= amount;
            return true;
        }
        return false;
    }
}
