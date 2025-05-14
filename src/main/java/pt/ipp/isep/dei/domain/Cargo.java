package pt.ipp.isep.dei.domain;

public class Cargo {
    private final CargoType type;
    private final double quantity;

    public Cargo(CargoType type, double quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    public CargoType getType() {
        return type;
    }

    public double getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return quantity + " units of " + type.name();
    }
}
