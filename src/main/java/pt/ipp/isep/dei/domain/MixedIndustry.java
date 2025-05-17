package pt.ipp.isep.dei.domain;
import pt.ipp.isep.dei.domain.Cargo;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Industry;
import pt.ipp.isep.dei.domain.Location;

import java.util.Objects;

public class MixedIndustry implements Industry {
    private final String industryName;
    private final int industryCost;
    private double production;
    private double demand;
    private Location location;
    private final Cargo inputCargo;
    private final Cargo outputCargo;

    /**
     * Construtor completo, incluindo localização.
     */
    public MixedIndustry(String industryName,
                         int industryCost,
                         double production,
                         double demand,
                         Location location,
                         CargoType inputCargoType,
                         CargoType outputCargoType) {
        Objects.requireNonNull(inputCargoType, "Tipo de carga de entrada não pode ser nulo");
        Objects.requireNonNull(outputCargoType, "Tipo de carga de saída não pode ser nulo");
        this.industryName = industryName;
        this.industryCost = industryCost;
        this.production   = production;
        this.demand       = demand;
        this.location     = location;
        this.inputCargo   = new Cargo(inputCargoType,  0.0);
        this.outputCargo  = new Cargo(outputCargoType, 0.0);
    }

    /**
     * Construtor simplificado (sem localização).
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

    @Override
    public void produceCargo() {
        if (inputCargo.getQuantity() >= demand) {
            outputCargo.addQuantity(production);
            inputCargo.removeQuantity(demand);
        }
    }

    @Override
    public void consumeCargo(double quantity) {
        inputCargo.removeQuantity(quantity);
    }

    @Override
    public Cargo getCargo() {
        return outputCargo;
    }

    /** Quantidade em estoque de carga de entrada. */
    public double getInputCargoQuantity() {
        return inputCargo.getQuantity();
    }

    /** Quantidade em estoque de carga de saída. */
    public double getOutputCargoQuantity() {
        return outputCargo.getQuantity();
    }

    /** Tipo de carga de entrada. */
    public CargoType getInputCargoType() {
        return inputCargo.getType();
    }

    /** Tipo de carga de saída. */
    public CargoType getOutputCargoType() {
        return outputCargo.getType();
    }

    /** Ajusta a taxa de produção. */
    public void setProduction(double production) {
        this.production = production;
    }

    /** Ajusta a demanda mensal. */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        if (demand > 0) {
            return String.format(
                    "MixedIndustry[%s] Consumes %s:%.2f -> Produces %s:%.2f",
                    industryName,
                    inputCargo.getType(),  inputCargo.getQuantity(),
                    outputCargo.getType(), outputCargo.getQuantity()
            );
        } else {
            return String.format(
                    "MixedIndustry[%s] Produces %s:%.2f",
                    industryName,
                    outputCargo.getType(), outputCargo.getQuantity()
            );
        }
    }
    // … dentro de ProducingIndustry …
    /**
     * Altera o tipo de carga produzida.
     */
    public void setInputCargo(CargoType newType) {
        Objects.requireNonNull(newType, "Tipo de carga não pode ser nulo");
        // substitui o tipo interno
        this.inputCargo.setCargoType(newType);
    }
    public void setOutputCargo(CargoType newType) {
        Objects.requireNonNull(newType, "Tipo de carga não pode ser nulo");
        this.outputCargo.setCargoType(newType);
    }

}
