package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Indústria que transforma um tipo de carga em outro.
 */
public class TransformingIndustry implements Industry {
    private final String industryName;
    private final int industryCost;
    private double production;
    private double demand;
    private Location location;
    private final Cargo inputCargo;
    private final Cargo outputCargo;

    /**
     * Construtor principal (sem localização).
     *
     * @param industryName Nome da indústria
     * @param industryCost Custo fixo de operação
     * @param production   Quantidade produzida por ciclo
     * @param demand       Quantidade de entrada necessária
     * @param inputType    Tipo de carga de entrada
     * @param outputType   Tipo de carga de saída
     */
    public TransformingIndustry(String industryName,
                                int industryCost,
                                double production,
                                double demand,
                                CargoType inputType,
                                CargoType outputType) {
        this(industryName, industryCost, production, demand, null, inputType, outputType);
    }

    /**
     * Construtor completo, incluindo localização.
     *
     * @param industryName Nome da indústria
     * @param industryCost Custo fixo de operação
     * @param production   Quantidade produzida por ciclo
     * @param demand       Quantidade de entrada necessária
     * @param location     Localização no mapa
     * @param inputType    Tipo de carga de entrada
     * @param outputType   Tipo de carga de saída
     */
    public TransformingIndustry(String industryName,
                                int industryCost,
                                double production,
                                double demand,
                                Location location,
                                CargoType inputType,
                                CargoType outputType) {
        Objects.requireNonNull(inputType,  "Tipo de carga de entrada não pode ser nulo");
        Objects.requireNonNull(outputType, "Tipo de carga de saída não pode ser nulo");
        this.industryName  = industryName;
        this.industryCost  = industryCost;
        this.production    = production;
        this.demand        = demand;
        this.location      = location;
        this.inputCargo    = new Cargo(inputType,  0.0);
        this.outputCargo   = new Cargo(outputType, 0.0);
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

    /** Retorna o tipo de carga de entrada. */
    public CargoType getInputCargoType() {
        return inputCargo.getType();
    }

    /** Retorna o tipo de carga de saída. */
    public CargoType getOutputCargoType() {
        return outputCargo.getType();
    }

    /** Ajusta a taxa de produção. */
    public void setProduction(double production) {
        this.production = production;
    }

    /** Ajusta a demanda de entrada. */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return String.format(
                "TransformingIndustry[%s] Consumes %s:%.2f -> Produces %s:%.2f",
                industryName,
                getInputCargoType(), inputCargo.getQuantity(),
                getOutputCargoType(), outputCargo.getQuantity()
        );
    }
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
