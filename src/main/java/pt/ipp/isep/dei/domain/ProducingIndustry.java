package pt.ipp.isep.dei.domain;

import java.util.Objects;

/**
 * Indústria que produz um único tipo de carga.
 */
public class ProducingIndustry implements Industry {
    private final String industryName;
    private final int industryCost;
    private double production;
    private double demand;
    private Location location;
    private final Cargo cargoStock;

    /**
     * Construtor principal (sem localização inicial).
     *
     * @param industryName Nome da indústria
     * @param industryCost Custo fixo de operação
     * @param production   Quantidade produzida por ciclo
     * @param demand       Demanda mínima para produzir
     * @param cargoType    Tipo de carga produzida
     */
    public ProducingIndustry(String industryName,
                             int industryCost,
                             double production,
                             double demand,
                             CargoType cargoType) {
        this(industryName, industryCost, production, demand, null, cargoType);
    }

    /**
     * Construtor completo, incluindo localização.
     *
     * @param industryName Nome da indústria
     * @param industryCost Custo fixo de operação
     * @param production   Quantidade produzida por ciclo
     * @param demand       Demanda mínima para produzir
     * @param location     Localização no mapa
     * @param cargoType    Tipo de carga produzida
     */
    public ProducingIndustry(String industryName,
                             int industryCost,
                             double production,
                             double demand,
                             Location location,
                             CargoType cargoType) {
        Objects.requireNonNull(cargoType, "Tipo de carga não pode ser nulo");
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

    @Override
    public void produceCargo() {
        cargoStock.addQuantity(production);
    }

    @Override
    public void consumeCargo(double quantity) {
        cargoStock.removeQuantity(quantity);
    }

    @Override
    public Cargo getCargo() {
        return cargoStock;
    }

    /** Retorna o tipo de carga produzida. */
    public CargoType getCargoType() {
        return cargoStock.getType();
    }

    /** Ajusta a taxa de produção. */
    public void setProduction(double production) {
        this.production = production;
    }

    /** Ajusta a demanda mínima. */
    public void setDemand(double demand) {
        this.demand = demand;
    }

    @Override
    public String toString() {
        return String.format(
                "ProducingIndustry[%s] Produces %s:%.2f",
                industryName,
                getCargoType(),
                getCargoQuantity()
        );
    }
    // … dentro de ProducingIndustry …
    /**
     * Altera o tipo de carga produzida.
     */
    public void setCargoType(CargoType newType) {
        Objects.requireNonNull(newType, "Tipo de carga não pode ser nulo");
        // substitui o tipo interno
        this.cargoStock.setCargoType(newType);
    }

}
