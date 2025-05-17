package pt.ipp.isep.dei.domain;

public class Cargo {
    private  CargoType type;
    private  double quantity=0;

    public Cargo(CargoType type, double quantity) {
        this.type = type;
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
    public void setQuantity(double value) {
        this.quantity = value;
    }
    public void setCargoType(CargoType type) {
        this.type = type;
    }
}
