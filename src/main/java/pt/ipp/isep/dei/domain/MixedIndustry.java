package pt.ipp.isep.dei.domain;

public class MixedIndustry implements Industry {
    private String industryName;
    private int industryCost;
    private double production;
    private double  demand;
    private Location location;
    private Cargo inputCargo;
    private Cargo outputCargo;

    public MixedIndustry(String getindustryname, int industryCost, double production, double demand, Location location,CargoType inputCargo,CargoType outputCargo ) {
        this.industryName = getindustryname;
        this.industryCost = industryCost;
        this.production = production;
        this.demand = demand;
        this.location = location;
        this.inputCargo = new Cargo(inputCargo, 0.0);
        this.outputCargo = new Cargo(outputCargo, 0.0);
    }

    public MixedIndustry(String getindustryname, int industryCost, double production, double demand, CargoType inputCargoType, CargoType outputCargoType) {
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

    public void setProduction(double newRate) {
        this.production = newRate;
    }
}



