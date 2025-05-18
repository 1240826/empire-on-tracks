package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Industry that produces a single type of cargo each cycle.
 * <p>
 * A ProducingIndustry generates cargo at a fixed rate per simulation tick,
 * tracks a minimum demand threshold before production, and maintains
 * an internal stock of produced cargo.
 * </p>
 */
public class ProducingIndustry implements Industry {

    /** Unique name of the industry. */
    private final String industryName;

    /** Fixed operating cost of the industry. */
    private final int industryCost;

    /** Quantity of cargo produced per cycle. */
    private double production;

    /** Minimum cargo demand required to enable production. */
    private double demand;

    /** Location of this industry on the map grid (may be null if unplaced). */
    private Location location;

    /** Internal stock of produced cargo. */
    private final Cargo cargoStock;

    /**
     * Constructs a ProducingIndustry without an initial location.
     *
     * @param industryName the unique name of the industry
     * @param industryCost the fixed operating cost per cycle
     * @param production   the amount of cargo produced per cycle
     * @param demand       the minimum demand threshold to produce cargo
     * @param cargoType    the {@link CargoType} of the produced cargo
     * @throws NullPointerException if {@code cargoType} is null
     */
    public ProducingIndustry(String industryName,
                             int industryCost,
                             double production,
                             double demand,
                             CargoType cargoType) {
        this(industryName, industryCost, production, demand, null, cargoType);
    }

    /**
     * Constructs a ProducingIndustry with an assigned location.
     *
     * @param industryName the unique name of the industry
     * @param industryCost the fixed operating cost per cycle
     * @param production   the amount of cargo produced per cycle
     * @param demand       the minimum demand threshold to produce cargo
     * @param location     the {@link Location} where the industry is placed
     * @param cargoType    the {@link CargoType} of the produced cargo
     * @throws NullPointerException if {@code cargoType} is null
     */
    public ProducingIndustry(String industryName,
                             int industryCost,
                             double production,
                             double demand,
                             Location location,
                             CargoType cargoType) {
        Objects.requireNonNull(cargoType, "Cargo type cannot be null");
        this.industryName = industryName;
        this.industryCost = industryCost;
        this.production   = production;
        this.demand       = demand;
        this.location     = location;
        this.cargoStock   = new Cargo(cargoType, 0.0);
    }

    @Override
    public String getIndustryName() {
        return industryName;
    }

    @Override
    public int getIndustryCost() {
        return industryCost;
    }

    @Override
    public double getProduction() {
        return production;
    }

    @Override
    public double getDemand() {
        return demand;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    /**
     * Produces cargo unconditionally at the specified production rate.
     * <p>
     * Each call adds {@code production} units to the internal cargo stock.
     * </p>
     */
    @Override
    public void produceCargo() {
        cargoStock.addQuantity(production);
    }

    /**
     * Consumes a specified quantity of cargo from internal stock.
     *
     * @param quantity the amount of cargo to remove
     */
    @Override
    public void consumeCargo(double quantity) {
        cargoStock.removeQuantity(quantity);
    }

    /**
     * Retrieves the internal cargo stock of this industry.
     *
     * @return the {@link Cargo} representing stored cargo
     */
    @Override
    public Cargo getCargo() {
        return cargoStock;
    }

    /**
     * Returns the type of cargo produced by this industry.
     *
     * @return the {@link CargoType} of produced cargo
     */
    public CargoType getCargoType() {
        return cargoStock.getType();
    }

    /**
     * Sets the production rate per cycle.
     *
     * @param production the new production amount
     */
    public void setProduction(double production) {
        this.production = production;
    }

    /**
     * Sets the minimum demand threshold required to start production.
     *
     * @param demand the new demand threshold
     */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    /**
     * Updates the type of cargo produced and resets stock if type changes.
     *
     * @param newType the new {@link CargoType} for production
     * @throws NullPointerException if {@code newType} is null
     */
    public void setCargoType(CargoType newType) {
        Objects.requireNonNull(newType, "Cargo type cannot be null");
        cargoStock.setCargoType(newType);
    }

    /**
     * Returns a summary of this industry including current cargo stock.
     *
     * @return a formatted {@code String} describing production status
     */
    @Override
    public String toString() {
        return String.format(
                "ProducingIndustry[%s] Produces %s:%.2f",
                industryName,
                getCargoType(),
                cargoStock.getQuantity()
        );
    }
}
