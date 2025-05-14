package pt.ipp.isep.dei.domain;

public class ProducingIndustry implements Industry {
    private String industryname;
    private int IndustryCost;
    private double production;

    public ProducingIndustry(String industryname, int IndustryCost, double production) {
        this.industryname = industryname;
        this.IndustryCost = IndustryCost;
        this.production = production;
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
}
