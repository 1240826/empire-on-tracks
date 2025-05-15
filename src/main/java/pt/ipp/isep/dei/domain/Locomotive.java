package pt.ipp.isep.dei.domain;

public class Locomotive {
    private String id;
    private String type;
    private double price;
    private boolean requiresElectrification;

    public Locomotive(String id, String type, double price, boolean requiresElectrification) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.requiresElectrification = requiresElectrification;
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
}


