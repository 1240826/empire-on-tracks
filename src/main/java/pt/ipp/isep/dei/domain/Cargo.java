package pt.ipp.isep.dei.domain;

import java.text.DecimalFormat;

/**
 * Represents a quantity of a specific cargo type in the simulation.
 * <p>
 * Cargo tracks the resource type and current amount stored.
 * Provides methods to add, remove, and reset quantities.
 * </p>
 */
public class Cargo {

    /** The type of cargo. */
    private CargoType type;

    /** Current stored quantity of this cargo. */
    private double quantity = 0;

    /**
     * Constructs a Cargo instance of the given type with an initial quantity.
     *
     * @param type     the {@link CargoType} of this cargo
     * @param quantity the starting quantity (non-negative)
     * @throws NullPointerException     if {@code type} is null
     * @throws IllegalArgumentException if {@code quantity} is negative
     */
    public Cargo(CargoType type, double quantity) {
        if (type == null) {
            throw new NullPointerException("Cargo type cannot be null");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.type = type;
        this.quantity = quantity;
    }

    /**
     * Returns the type of this cargo.
     *
     * @return the {@link CargoType}
     */
    public CargoType getType() {
        return type;
    }

    /**
     * Returns the current stored quantity of this cargo.
     *
     * @return the quantity (non-negative)
     */
    public double getQuantity() {
        return quantity;
    }

    /**
     * Sets the stored quantity to the specified value.
     *
     * @param value the new quantity (must be non-negative)
     * @throws IllegalArgumentException if {@code value} is negative
     */
    public void setQuantity(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        this.quantity = value;
    }

    /**
     * Sets the cargo type.
     * <p>
     * Does not change the current quantity.
     * </p>
     *
     * @param type the new {@link CargoType}
     * @throws NullPointerException if {@code type} is null
     */
    public void setCargoType(CargoType type) {
        if (type == null) {
            throw new NullPointerException("Cargo type cannot be null");
        }
        this.type = type;
    }

    /**
     * Adds the specified positive amount to the cargo quantity.
     *
     * @param value the amount to add (must be positive)
     */
    public void addQuantity(double value) {
        if (value > 0) {
            this.quantity += value;
        }
    }

    /**
     * Attempts to remove the specified amount from the cargo quantity.
     *
     * @param value the amount to remove (must be positive)
     * @return {@code true} if removal succeeded; {@code false} otherwise
     */
    public boolean removeQuantity(double value) {
        if (value > 0 && this.quantity >= value) {
            this.quantity -= value;
            return true;
        }
        return false;
    }

    /**
     * Returns a human-readable string of cargo quantity and type.
     *
     * @return formatted string like "123.45 units of COAL"
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(quantity) + " units of " + type.name();
    }
}