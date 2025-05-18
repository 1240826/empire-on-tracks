// File: pt/ipp/isep/dei/domain/Locomotive.java
package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a locomotive, with technical specifications, costs, and
 * the ability to move along a route of stations, unload and load cargo.
 */
public class Locomotive {
    // Identification & economics
    private final String id;
    private final String type;                  // "Steam", "Diesel", "Electric"
    private final double price;
    private final boolean requiresElectrification;
    private List<Station> route = List.of();
    private int currentIndex = 0;


    // Technical specs
    private final int power;                    // kW or HP
    private final double acceleration;          // m/s² or similar
    private final double topSpeed;              // km/h
    private final int startYear;                // Year in which locomotive becomes available

    // Operating costs
    private final double fuelCostPerYear;       // e.g., €/year
    private final double maintenancePerYear;    // e.g., €/year


    private final double capacity = 0;              // max cargo volume the loco can carry
    private final List<Cargo> currentLoad = new ArrayList<>();

    // scenario availability
    private Period availability;

    /**
     * Constructs a new Locomotive instance.
     */
    public Locomotive(String id,
                      String type,
                      double price,
                      boolean requiresElectrification,
                      int power,
                      double acceleration,
                      double topSpeed,
                      int startYear,
                      double fuelCostPerYear,
                      double maintenancePerYear) {
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
        setRoute(route);
    }

    // Getters
    public String getId()                    { return id; }
    public String getType()                  { return type; }
    public double getPrice()                 { return price; }
    public boolean requiresElectrification() { return requiresElectrification; }
    public int getPower()                    { return power; }
    public double getAcceleration()          { return acceleration; }
    public double getTopSpeed()              { return topSpeed; }
    public int getStartYear()                { return startYear; }
    public double getFuelCostPerYear()       { return fuelCostPerYear; }
    public double getMaintenancePerYear()    { return maintenancePerYear; }
    public double getCapacity()              { return capacity; }

    /** Returns an unmodifiable copy of the route. */
    public List<Station> getRoute()          { return List.copyOf(route); }

    /** Sets a new route and resets position to the first station. */
    public void setRoute(List<Station> route) {
        this.route = new ArrayList<>(route);
        this.currentIndex = 0;
    }
    public Station getCurrentStation() {
        return route.isEmpty() ? null : route.get(currentIndex);
    }
    public void move() {
        if (!route.isEmpty())
            currentIndex = (currentIndex + 1) % route.size();
    }




    /**
     * Unloads all cargo to the current station and clears internal load.
     */
    public void unloadToStation() {
        Station station = getCurrentStation();
        if (station == null) return;
        for (Cargo c : new ArrayList<>(currentLoad)) {
            double qty = c.getQuantity();
            if (qty > 0) {
                station.unloadCargo(c.getType(), qty);
                c.setQuantity(0);
            }
        }
        currentLoad.removeIf(c -> c.getQuantity() == 0);
    }

    /**
     * Collects cargo from the current station up to capacity.
     */



    /** Remaining capacity. */
    public double spaceLeft() {
        double used = currentLoad.stream().mapToDouble(Cargo::getQuantity).sum();
        return capacity - used;
    }

    private void addToLoad(CargoType type, double qty) {
        for (Cargo c : currentLoad) {
            if (c.getType() == type) {
                c.addQuantity(qty);
                return;
            }
        }
        currentLoad.add(new Cargo(type, qty));
    }

    public Period getAvailability()         { return availability; }
    public void setAvailability(Period a)   { this.availability = a; }

    @Override
    public String toString() {
        return String.format(
                "Locomotive[id=%s,type=%s,at=%s,load=%s]",
                id,
                type,
                Optional.ofNullable(getCurrentStation()).map(Station::getStationName).orElse("none"),
                currentLoad
        );
    }
}