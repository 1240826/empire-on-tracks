package pt.ipp.isep.dei.domain;

public class MixedIndustry implements Industry {
    private String industryName;
    private int industryCost;
    private double production;
    private double  demand;
    private Location location;
    private CargoType inputCargo;
    private CargoType outputCargo;

    public MixedIndustry(String name, int cost, double production, double demand,CargoType inputCargo,CargoType outputCargo) {
        this.industryName = name;
        this.industryCost = cost;
        this.production = production;
        this.demand = demand;
        this.inputCargo = inputCargo;
        this.outputCargo = outputCargo;
    }

    public MixedIndustry(String getindustryname, int industryCost, double production, double demand, Location location) {
        this.industryName = getindustryname;
        this.industryCost = industryCost;
        this.production = production;
        this.demand = demand;
        this.location = location;
        this.inputCargo = CargoType.OIL;
        this.outputCargo = CargoType.COAL;
        if (demand > 0) {
            this.inputCargo = CargoType.OIL;
        }
    }

    @Override
    public String getindustryname() {
        return new String(this.industryName);
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
    public void setLocation(Location location) {
        this.location = location;
    }
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Cargo produceCargo() {
        return new Cargo(outputCargo, production);
    }
    @Override
    public Cargo consumeCargo() {
        if (demand > 0) {
            return new Cargo(inputCargo, demand);
        } else {
            return null; // Atua como produtora apenas
        }
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
    @Override
    public String toString() {
        if (demand > 0) {
            return "Transforming (Mixed): " + industryName +
                    " | Consumes: " + inputCargo + " (" + demand + ")" +
                    ", Produces: " + outputCargo + " (" + production + ")";
        } else {
            return "Producing (Mixed): " + industryName +
                    " | Produces: " + outputCargo + " (" + production + ")";
        }
    }

}



