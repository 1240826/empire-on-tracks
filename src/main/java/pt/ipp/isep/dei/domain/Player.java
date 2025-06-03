package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.user.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Player extends User {

    private double budget;
    private final List<Locomotive> ownedLocomotives = new ArrayList<>();
    private final List<Train> ownedTrains = new ArrayList<>();
    private Scenario selectedScenario;

    public Player(String username, String password, double initialBudget) {
        super(username, password);
        if (initialBudget < 0) throw new IllegalArgumentException("Initial budget cannot be negative");
        this.budget = initialBudget;
    }

    public double getBudget() { return budget; }

    public void setBudget(double budget) {
        if (budget < 0) throw new IllegalArgumentException("Budget cannot be negative");
        this.budget = budget;
    }

    public boolean hasSufficientBudget(double price) { return budget >= price; }

    public Locomotive buyLocomotive(Locomotive loco) {
        Objects.requireNonNull(loco, "Locomotive cannot be null");
        if (hasSufficientBudget(loco.getPrice())) {
            budget -= loco.getPrice();
            ownedLocomotives.add(loco);
            return loco;
        }
        return null;
    }

    public List<Locomotive> getOwnedLocomotives() { return Collections.unmodifiableList(ownedLocomotives); }

    public void addTrain(Train train) {
        Objects.requireNonNull(train, "Train cannot be null");
        ownedTrains.add(train);
    }

    public List<Train> getOwnedTrains() { return Collections.unmodifiableList(ownedTrains); }

    public boolean spend(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Spend amount cannot be negative");
        if (hasSufficientBudget(amount)) {
            budget -= amount;
            return true;
        }
        return false;
    }

    public Scenario getSelectedScenario() { return selectedScenario; }
    public void setSelectedScenario(Scenario selectedScenario) { this.selectedScenario = selectedScenario; }

    // [ADDED] Remove train utility
    public boolean removeTrain(Train train) {
        return ownedTrains.remove(train);
    }

    // [ADDED] Get train by name utility
    public Train getTrainByName(String name) {
        return ownedTrains.stream()
                .filter(t -> t.getName().equals(name))
                .findFirst().orElse(null);
    }
}
