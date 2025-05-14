package pt.ipp.isep.dei.domain;

public class MixedIndustry implements Industry {
    private String industryName;
    private int industryCost;
    private double production;
    private double  demand;
    private Location location;

    public MixedIndustry(String name, int cost, double production, double demand) {
        this.industryName = name;
        this.industryCost = cost;
        this.production = production;
        this.demand = demand;
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
    @Override
    public Location getLocation() {
        return location;
    }

}
