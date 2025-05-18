package pt.ipp.isep.dei.domain;

public class DummyIndustry implements Industry {
    private final String name;
    private final Cargo cargo;
    private Location location;

    public DummyIndustry(String name, Location location, Cargo cargo) {
        this.name = name;
        this.location = location;
        this.cargo = cargo;
    }

    @Override public String getIndustryName() { return name; }
    @Override public int getIndustryCost() { return 0; }
    @Override public double getProduction() { return 0; }
    @Override public double getDemand() { return 0; }
    @Override public void setLocation(Location location) { this.location = location; }
    @Override public Location getLocation() { return location; }
    @Override public void produceCargo() {}
    @Override public void consumeCargo(double quantity) { cargo.removeQuantity(quantity); }
    @Override public Cargo getCargo() { return cargo; }
}