package pt.ipp.isep.dei.domain;

public class Cargo {
    private final CargoType type;
    private  double quantity;

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

    public void addQuantity(double value) {
        if (value > 0) {
            this.quantity += value;
        }
    }

    public boolean removeQuantity(double value) {
        if (value > 0 && this.quantity >= value) {
            this.quantity -= value;
            return true;
        }
        return false;
    }
}
