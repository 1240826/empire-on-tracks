package pt.ipp.isep.dei.domain;

public class ProducingIndustry implements Industry {
    private String industryname;
    private int IndustryCost;
    private double production;
    private double  demand;
    private Location location;


    public ProducingIndustry(String industryname, int IndustryCost, double production,double demand) {
        this.industryname = industryname;
        this.IndustryCost = IndustryCost;
        this.production = production;
        this.demand = demand;

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
}
