package pt.ipp.isep.dei.domain;

/**
 * Common interface for different types of industries in the simulation.
 * <p>
 * Defines the contract for production, consumption, cost, and location tracking.
 * Implementing classes may represent industries that produce, transform,
 * or handle cargo in various ways.
 * </p>
 */
public interface Industry {

    /**
     * Returns the unique name of this industry.
     *
     * @return the industry name
     */
    String getIndustryName();

    /**
     * Returns the fixed operating cost of this industry per cycle.
     *
     * @return the operating cost
     */
    int getIndustryCost();

    /**
     * Returns the quantity of cargo produced or transformed each cycle.
     *
     * @return the production rate
     */
    double getProduction();

    /**
     * Returns the demand threshold required to enable production or transformation.
     * May be zero for industries with no input requirements.
     *
     * @return the demand amount
     */
    double getDemand();

    /**
     * Sets the location of this industry on the simulation map.
     *
     * @param location the {@link Location} to place the industry
     */
    void setLocation(Location location);

    /**
     * Returns the current location of this industry on the simulation map.
     *
     * @return the {@link Location} of the industry, or null if unplaced
     */
    Location getLocation();

    /**
     * Executes one production or transformation cycle.
     * <p>
     * Implementations should handle input consumption, output generation,
     * and any internal state updates required per simulation tick.
     * </p>
     */
    void produceCargo();

    /**
     * Consumes a specified quantity of input cargo from internal storage.
     * Only applicable to industries that require input resources.
     *
     * @param quantity the amount of cargo to consume
     */
    void consumeCargo(double quantity);

    /**
     * Returns the {@link Cargo} object representing output or current stock.
     *
     * @return the cargo storage for this industry
     */
    Cargo getCargo();

    /**
     * Returns the current quantity in the cargo storage returned by {@link #getCargo()}.
     *
     * @return the cargo quantity
     */
    default double getCargoQuantity() {
        return getCargo().getQuantity();
    }
}