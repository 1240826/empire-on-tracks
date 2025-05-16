package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

public class City {
    private String name;
    private int positionX;
    private int positionY;
    private int houseBlocksCount;        // número desejado de blocos
    private List<HouseBlock> houseBlocksList = new ArrayList<>();
    private Location center;

    private City(String name, int positionX, int positionY, int houseBlocksCount) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.houseBlocksCount = houseBlocksCount;
        this.center = new Location(positionX, positionY);
    }

    public static City createCity(String name, int positionX, int positionY, int houseBlocksCount) {
        City c = new City(name, positionX, positionY, houseBlocksCount);
        if (!c.validateName()) {
            throw new IllegalArgumentException("Invalid city name: " + name);
        }
        if (!c.validateHouseBlocks()) {
            throw new IllegalArgumentException("Invalid number of house blocks: " + houseBlocksCount);
        }
        return c;
    }

    public boolean validateName() {
        return name != null && !name.trim().isEmpty();
    }

    public boolean validateHouseBlocks() {
        return houseBlocksCount > 0;
    }

    /** gera blocos em volta do centro usando distribuição normal */
    public void assignHouseBlocksAutomatic() {
        for (int i = 0; i < houseBlocksCount; i++) {
            Location pos = Location.computeBlockPosition(center);
            houseBlocksList.add(HouseBlock.createAt(pos));
        }
    }

    /** usa as posições passadas pelo utilizador */
    public void assignHouseBlocksManually(List<Location> positions) {
        if (positions.size() != houseBlocksCount) {
            throw new IllegalArgumentException("Number of positions must match house blocks count.");
        }
        for (Location pos : positions) {
            houseBlocksList.add(HouseBlock.createAt(pos));
        }
    }

    // getters simples
    public String getName() { return name; }
    public int getPositionX() { return positionX; }
    public int getPositionY() { return positionY; }
    public Location getCenter()  { return center; }
    public List<HouseBlock> getHouseBlocks() { return houseBlocksList; }
}
