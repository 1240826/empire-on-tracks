package pt.ipp.isep.dei.domain;

public class TransformingIndustry implements Industry {
    private String industryName;
    private int industryCost;
    private float production;
    private float demand;

    public TransformingIndustry(String name, int cost, float production, float demand) {
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

}
