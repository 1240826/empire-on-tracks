package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a locomotive, with technical specifications, costs, and
 * the ability to move along a route of stations and collect cargo.
 */
public class Locomotive {
    // Identification & economics
    private final String id;
    private final String type;                  // "Steam", "Diesel", "Electric"
    private final double price;
    private final boolean requiresElectrification;

    // Technical specs
    private final int power;                    // kW or HP
    private final double acceleration;          // m/s² or similar
    private final double topSpeed;              // km/h
    private final int startYear;                // Year in which locomotive becomes available

    // Operating costs
    private final double fuelCostPerYear;       // e.g., €/year
    private final double maintenancePerYear;    // e.g., €/year

    // Routing & cargo
    private final List<Station> route;          // sequence of stations to visit
    private int currentIndex = 0;               // index in the route
    private final double capacity;              // max cargo volume the loco can carry
    private final List<Cargo> currentLoad = new ArrayList<>();

    public Locomotive(String id,
                      String type,
                      double price,
                      boolean requiresElectrification,
                      int power,
                      double acceleration,
                      double topSpeed,
                      int startYear,
                      double fuelCostPerYear,
                      double maintenancePerYear,
                      double capacity,
                      List<Station> route) {
        this.id                      = Objects.requireNonNull(id);
        this.type                    = Objects.requireNonNull(type);
        this.price                   = price;
        this.requiresElectrification = requiresElectrification;
        this.power                   = power;
        this.acceleration            = acceleration;
        this.topSpeed                = topSpeed;
        this.startYear               = startYear;
        this.fuelCostPerYear         = fuelCostPerYear;
        this.maintenancePerYear      = maintenancePerYear;
        this.capacity                = capacity;
        this.route                   = new ArrayList<>(Objects.requireNonNull(route));
    }

    // Getters
    public String getId()                       { return id; }
    public String getType()                     { return type; }
    public double getPrice()                    { return price; }
    public boolean requiresElectrification()    { return requiresElectrification; }
    public int getPower()                       { return power; }
    public double getAcceleration()             { return acceleration; }
    public double getTopSpeed()                 { return topSpeed; }
    public int getStartYear()                   { return startYear; }
    public double getFuelCostPerYear()          { return fuelCostPerYear; }
    public double getMaintenancePerYear()       { return maintenancePerYear; }
    public double getCapacity()                 { return capacity; }
    public List<Station> getRoute()             { return new ArrayList<>(route); }
    public List<Cargo> getCurrentLoad() {
        List<Cargo> copy = new ArrayList<>();
        for (Cargo c : currentLoad) {
            copy.add(new Cargo(c.getType(), c.getQuantity()));
        }
        return copy;
    }

    /**
     * Move to the next station on the route (circular).
     */
    public void move() {
        if (!route.isEmpty()) {
            currentIndex = (currentIndex + 1) % route.size();
        }
    }

    /**
     * Returns true if the locomotive is currently at a station
     * (i.e., its route is non-empty).
     */
    public boolean hasStationNearby() {
        return !route.isEmpty();
    }

    /**
     * Collects cargo from the current station until the locomotive is full.
     * Removes the collected quantity from the station.
     */
    public void collectFromStation() {
        if (route.isEmpty()) {
            return;
        }
        Station station = route.get(currentIndex);
        for (Cargo stCargo : station.getCargoes()) {
            double available = stCargo.getQuantity();
            if (available <= 0) {
                continue;
            }
            double canTake = Math.min(available, spaceLeft());
            if (canTake <= 0) {
                break; // locomotive is full
            }
            // 1) remove from station
            stCargo.removeQuantity(canTake);
            // 2) add to locomotive load
            boolean added = false;
            for (Cargo c : currentLoad) {
                if (c.getType() == stCargo.getType()) {
                    c.addQuantity(canTake);
                    added = true;
                    break;
                }
            }
            if (!added) {
                currentLoad.add(new Cargo(stCargo.getType(), canTake));
            }
        }
    }

    /**
     * Calculates remaining capacity.
     */
    private double spaceLeft() {
        double used = currentLoad.stream()
                .mapToDouble(Cargo::getQuantity)
                .sum();
        return capacity - used;
    }

    @Override
    public String toString() {
        return String.format(
                "Locomotive[id=%s, type=%s, price=%.2f, electrified=%b, power=%d, accel=%.2f, topSpeed=%.2f, startYear=%d, fuelCost=%.2f, maintenance=%.2f, capacity=%.2f, load=%s]",
                id, type, price, requiresElectrification, power, acceleration, topSpeed, startYear, fuelCostPerYear, maintenancePerYear, capacity, currentLoad
        );
    }
}
