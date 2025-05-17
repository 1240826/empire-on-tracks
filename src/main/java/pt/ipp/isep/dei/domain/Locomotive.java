package pt.ipp.isep.dei.domain;

/**
 * Represents a locomotive, with technical specifications and requirements.
 */
public class Locomotive {
    private String id;
    private String type; // "Steam", "Diesel", "Electric"
    private double price;
    private boolean requiresElectrification;

    // New attributes for US9/US11 spec
    private int power;                 // kW or HP
    private double acceleration;       // m/s² or similar
    private double topSpeed;           // km/h
    private int startYear;             // Year in which locomotive becomes available
    private double fuelCostPerYear;    // e.g., €/year
    private double maintenancePerYear; // e.g., €/year

    public Locomotive(String id, String type, double price, boolean requiresElectrification,
                      int power, double acceleration, double topSpeed, int startYear,
                      double fuelCostPerYear, double maintenancePerYear) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.requiresElectrification = requiresElectrification;
        this.power = power;
        this.acceleration = acceleration;
        this.topSpeed = topSpeed;
        this.startYear = startYear;
        this.fuelCostPerYear = fuelCostPerYear;
        this.maintenancePerYear = maintenancePerYear;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public boolean requiresElectrification() {
        return requiresElectrification;
    }

    public int getPower() {
        return power;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double getTopSpeed() {
        return topSpeed;
    }

    public int getStartYear() {
        return startYear;
    }

    public double getFuelCostPerYear() {
        return fuelCostPerYear;
    }

    public double getMaintenancePerYear() {
        return maintenancePerYear;
    }

    // Optionally, toString() for debugging
    @Override
    public String toString() {
        return String.format(
                "Locomotive[id=%s, type=%s, price=%.2f, electrified=%b, power=%d, acceleration=%.2f, topSpeed=%.2f, startYear=%d, fuelCost=%.2f, maintenance=%.2f]",
                id, type, price, requiresElectrification, power, acceleration, topSpeed, startYear, fuelCostPerYear, maintenancePerYear
        );
    }
}

