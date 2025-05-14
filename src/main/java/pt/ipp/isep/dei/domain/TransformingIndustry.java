package pt.ipp.isep.dei.domain;

public class TransformingIndustry implements Industry {
    private String industryName;
    private int industryCost;
    private double production;
    private double demand;
    private Location location;
    private CargoType inputCargo;
    private CargoType outputCargo;

    public TransformingIndustry(String name, int cost, double production, double ,CargoType inputCargo,CargoType outputCargo ) {
        this.industryName = name;
        this.industryCost = cost;
        this.production = production;
        this.demand = demand;
        this.inputCargo = inputCargo;
        this.outputCargo = outputCargo;

    }


    @Override
    public String getindustryname() {
        return new String(this.industryName);
    }

    @Override
    public int getIndustryCost() {
        return industryCost;
    }

    public String toString() {
        return "Producing: " + this.getindustryname() + " (Cost: " + this.getIndustryCost() + ")";
    }

    @Override
    public double getProduction() {
        return production;
    }
    @Override
    public double getDemand() {
        return demand;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }

    @Override
    public Cargo produceCargo() {
        return new Cargo(outputCargo, production);
    }
    public Cargo consumeCargo() {
        return new Cargo(inputCargo, demand);
    }


    public void setInputCargoType(CargoType cargoType) {
        this.inputCargo = cargoType;
    }
    public void setOutputCargoType(CargoType cargoType) {
        this.outputCargo = cargoType;
    }

    public CargoType getInputCargoType () {
        return inputCargo;
    }
    public CargoType getOutputCargoType () {
        return outputCargo;
    }
}

