package pt.ipp.isep.dei.domain;

/**
 * Represents a player in the system, with a username, password, and budget.
 */
public class Player extends User {

    private double budget;

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
}
