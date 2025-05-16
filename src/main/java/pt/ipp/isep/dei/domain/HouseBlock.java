package pt.ipp.isep.dei.domain;


public class HouseBlock {
    private Location position;
    private HouseBlock(Location position) { this.position = position; }
    public static HouseBlock createAt(Location position) {
        return new HouseBlock(position);
    }
    public Location getPosition() { return position; }
}

