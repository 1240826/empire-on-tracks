package pt.ipp.isep.dei.domain;

public class ProducingIndustry implements Industry {
    private String industryname;
    private int IndustryCost;
    private double production;
    private double  demand;
    private Location location;
    private CargoType cargoType;


    public ProducingIndustry(String industryname, int IndustryCost, double production,double demand,CargoType cargoType) {
        this.industryname = industryname;
        this.IndustryCost = IndustryCost;
        this.production = production;
        this.demand = demand;
        this.cargoType = cargoType;

    }
    @Override
    public String getindustryname() {
        return new String(this.industryname);
    }
    @Override
    public int getIndustryCost() {
        return IndustryCost;
    }
    public String toString() {
        return "Producing: " + industryname + " (Cost: " + getIndustryCost()+")";
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
        return new Cargo(cargoType, production);
    }


    public void setCargoType(CargoType cargoType) {
        this.cargoType = cargoType;
    }

    public CargoType getCargoType() {
        return cargoType;
    }
}
