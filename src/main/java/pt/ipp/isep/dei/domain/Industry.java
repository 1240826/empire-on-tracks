package pt.ipp.isep.dei.domain;

public interface Industry {
    public String getindustryname();
    public int getIndustryCost();
    public double getProduction();
    public double getDemand();
    void setLocation(Location location);
    Location getLocation();

    Cargo produceCargo();

    default Cargo consumeCargo() {
        return null;
    }

}
