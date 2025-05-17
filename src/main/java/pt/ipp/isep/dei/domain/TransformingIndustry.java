package pt.ipp.isep.dei.domain;

public class TransformingIndustry implements Industry {
    private String industryName;
    private int industryCost;
    private double production;
    private double demand;
    private Location location;
    private Cargo inputCargo;
    private Cargo outputCargo;


    public TransformingIndustry(String name, int cost, double production, double demand,CargoType inputCargo,CargoType outputCargo, Location location) {
        this.industryName = name;
        this.industryCost = cost;
        this.production = production;
        this.demand = demand;
        this.inputCargo = new Cargo(inputCargo, 0.0);
        this.outputCargo = new Cargo(outputCargo, 0.0);
        this.location = location;

    }

    public TransformingIndustry(String getindustryname, int industryCost, double production, double demand, CargoType input, CargoType output) {
        this.industryName = getindustryname;
        this.industryCost = industryCost;
        this.production = production;
        this.demand = demand;
        this.inputCargo = new Cargo(input, 0.0);
        this.outputCargo=new Cargo(output,0.0);
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

    public void consumeCargo(double quantity) {
        double resto = inputCargo.getQuantity()-quantity;
        inputCargo.setQuantity(resto);
    }
    public Cargo produceCargo() {
        if( inputCargo.getQuantity() >= demand ){
            outputCargo.addQuantity(production);
            inputCargo.removeQuantity(demand);
        }
        return null;
    }


    public void setInputCargoType(CargoType cargoType) {
        this.inputCargo.setCargoType(cargoType);
    }
    public void setOutputCargoType(CargoType cargoType) {
        this.outputCargo.setCargoType(cargoType);
    }

    public CargoType getInputCargoType () {
        return inputCargo.getType();
    }
    public CargoType getOutputCargoType () {
        return outputCargo.getType();
    }

}

