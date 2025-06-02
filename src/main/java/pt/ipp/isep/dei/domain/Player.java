package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    private Scenario selectedScenario;
    /**
     * Constructs a new Player with the given username, password and initial budget.
     *
     * @param username the username of the player
     * @param password the password of the player
     * @param initialBudget the initial budget value
     * @throws NullPointerException if username or password is null
     */
    public Player(String username, String password, double initialBudget) {
        super(username, password);
        if (initialBudget < 0) {
            throw new IllegalArgumentException("Initial budget cannot be negative");
        }
        this.budget = initialBudget;
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
     * @throws IllegalArgumentException if budget is negative
     */
    public void setBudget(double budget) {
        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        this.budget = budget;
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
        Objects.requireNonNull(loco, "Locomotive cannot be null");
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
     * Used when a player creates a train (US10).
     *
     * @param train the train to add
     * @throws NullPointerException if train is null
     */
    public void addTrain(Train train) {
        Objects.requireNonNull(train, "Train cannot be null");
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
        if (amount < 0) {
            throw new IllegalArgumentException("Spend amount cannot be negative");
        }
        if (hasSufficientBudget(amount)) {
            budget -= amount;
            return true;
        }
        return false;
    }

    public Scenario getSelectedScenario() {
        return selectedScenario;
    }

    public void setSelectedScenario(Scenario selectedScenario) {
        this.selectedScenario = selectedScenario;
    }
}
