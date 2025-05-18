package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Represents an industry that can both consume and produce cargo types within the simulation.
 * <p>
 * A MixedIndustry consumes input cargo when available and produces output cargo each cycle.
 * It can be placed on a map at a specified location and tracks costs and cargo quantities.
 * </p>
 */
public class MixedIndustry implements Industry {

    /** Name of the industry. */
    private final String industryName;

    /** Fixed operating cost of the industry. */
    private final int industryCost;

    /** Amount of output cargo produced per cycle. */
    private double production;

    /** Amount of input cargo required per cycle. */
    private double demand;

    /** Location of this industry on the map grid (may be null if unplaced). */
    private Location location;

    /** Internal storage for input cargo. */
    private final Cargo inputCargo;

    /** Internal storage for output cargo. */
    private final Cargo outputCargo;

    /**
     * Constructs a MixedIndustry with a specific location.
     *
     * @param industryName     the unique name of the industry
     * @param industryCost     the fixed operating cost per cycle
     * @param production       the quantity of cargo produced per cycle
     * @param demand           the quantity of input cargo required per cycle
     * @param location         the {@link Location} where the industry is placed
     * @param inputCargoType   the type of cargo consumed
     * @param outputCargoType  the type of cargo produced
     * @throws NullPointerException if {@code inputCargoType} or {@code outputCargoType} is null
     */
    public MixedIndustry(String industryName,
                         int industryCost,
                         double production,
                         double demand,
                         Location location,
                         CargoType inputCargoType,
                         CargoType outputCargoType) {
        Objects.requireNonNull(inputCargoType, "Input cargo type cannot be null");
        Objects.requireNonNull(outputCargoType, "Output cargo type cannot be null");
        this.industryName = industryName;
        this.industryCost = industryCost;
        this.production   = production;
        this.demand       = demand;
        this.location     = location;
        this.inputCargo   = new Cargo(inputCargoType,  0.0);
        this.outputCargo  = new Cargo(outputCargoType, 0.0);
    }

    /**
     * Constructs a MixedIndustry without an assigned location.
     *
     * @param industryName     the unique name of the industry
     * @param industryCost     the fixed operating cost per cycle
     * @param production       the quantity of cargo produced per cycle
     * @param demand           the quantity of input cargo required per cycle
     * @param inputCargoType   the type of cargo consumed
     * @param outputCargoType  the type of cargo produced
     */
    public MixedIndustry(String industryName,
                         int industryCost,
                         double production,
                         double demand,
                         CargoType inputCargoType,
                         CargoType outputCargoType) {
        this(industryName, industryCost, production, demand,
                null, inputCargoType, outputCargoType);
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
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Produces output cargo if sufficient input cargo is available.
     * <p>
     * Consumes {@code demand} units from input storage and adds {@code production}
     * units to output storage.
     * </p>
     */
    @Override
    public void produceCargo() {
        if (inputCargo.getQuantity() >= demand) {
            outputCargo.addQuantity(production);
            inputCargo.removeQuantity(demand);
        }
    }

    /**
     * Consumes a specified quantity of input cargo.
     *
     * @param quantity the amount of input cargo to consume
     */
    @Override
    public void consumeCargo(double quantity) {
        inputCargo.removeQuantity(quantity);
    }

    /**
     * Retrieves the output cargo storage.
     *
     * @return the {@link Cargo} representing produced cargo
     */
    @Override
    public Cargo getCargo() {
        return outputCargo;
    }

    /**
     * Gets the current stored quantity of input cargo.
     *
     * @return the input cargo quantity
     */
    public double getInputCargoQuantity() {
        return inputCargo.getQuantity();
    }

    /**
     * Gets the current stored quantity of output cargo.
     *
     * @return the output cargo quantity
     */
    public double getOutputCargoQuantity() {
        return outputCargo.getQuantity();
    }

    /**
     * Gets the type of cargo consumed by this industry.
     *
     * @return the {@link CargoType} for input cargo
     */
    public CargoType getInputCargoType() {
        return inputCargo.getType();
    }

    /**
     * Gets the type of cargo produced by this industry.
     *
     * @return the {@link CargoType} for output cargo
     */
    public CargoType getOutputCargoType() {
        return outputCargo.getType();
    }

    /**
     * Sets the per-cycle production amount.
     *
     * @param production the new production quantity per cycle
     */
    public void setProduction(double production) {
        this.production = production;
    }

    /**
     * Sets the per-cycle demand amount.
     *
     * @param demand the new input demand per cycle
     */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        if (demand > 0) {
            return String.format(
                    "MixedIndustry[%s] Consumes %s:%.2f -> Produces %s:%.2f",
                    industryName,
                    getInputCargoType(),  inputCargo.getQuantity(),
                    getOutputCargoType(), outputCargo.getQuantity()
            );
        } else {
            return String.format(
                    "MixedIndustry[%s] Produces %s:%.2f",
                    industryName,
                    getOutputCargoType(), outputCargo.getQuantity()
            );
        }
    }

    /**
     * Updates the input cargo type. Resets quantity if type changes.
     *
     * @param newType the new {@link CargoType} for input cargo
     * @throws NullPointerException if {@code newType} is null
     */
    public void setInputCargo(CargoType newType) {
        Objects.requireNonNull(newType, "Input cargo type cannot be null");
        this.inputCargo.setCargoType(newType);
    }

    /**
     * Updates the output cargo type. Resets quantity if type changes.
     *
     * @param newType the new {@link CargoType} for output cargo
     * @throws NullPointerException if {@code newType} is null
     */
    public void setOutputCargo(CargoType newType) {
        Objects.requireNonNull(newType, "Output cargo type cannot be null");
        this.outputCargo.setCargoType(newType);
    }

    /**
     * Sets the stored quantity of input cargo.
     *
     * @param quantity the new quantity for input cargo
     */
    public void setInputCargoQuantity(double quantity) {
        this.inputCargo.setQuantity(quantity);
    }
}