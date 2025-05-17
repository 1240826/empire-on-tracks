package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa o mapa do simulador, contendo estações, indústrias,
 * linhas férreas e locomotivas em uma grade 2D.
 */
public class Map {
    private String name;
    private final Object[][] map;
    private final List<RailLine> railLines       = new ArrayList<>();
    private final List<Station> stations         = new ArrayList<>();
    private final List<Industry> industries      = new ArrayList<>();
    private final List<Locomotive> locomotives   = new ArrayList<>();

    /** Construtor padrão: 10×10, nome "name". */
    public Map() {
        this("name", 10, 10);
    }

    /** Construtor com nome e dimensões da grade. */
    public Map(String name, int width, int height) {
        this.name = name;
        this.map  = new Object[height][width];
    }

    // --- Estações ---
    public void addStation(Station station) {
        stations.add(station);
    }

    public List<Station> getStations() {
        return new ArrayList<>(stations);
    }

    public List<String> getAllNamesStation() {
        List<String> names = new ArrayList<>();
        for (Station s : stations) {
            names.add(s.getStationName());
        }
        return names;
    }

    public Station getDetails(String stationName) {
        for (Station s : stations) {
            if (s.getStationName().equals(stationName)) {
                return s;
            }
        }
        return null;
    }

    // --- Linhas férreas ---
    public void addRailLine(RailLine line) {
        railLines.add(line);
    }

    public List<RailLine> getRailLines() {
        return new ArrayList<>(railLines);
    }

    // --- Indústrias ---
    /**
     * Coloca uma indústria no mapa. Retorna false se a posição estiver ocupada ou fora da grade.
     */
    public boolean addIndustry(Industry industry, Location location) {
        int x = location.getX(), y = location.getY();
        if (!isEmpty(x, y)) {
            return false;
        }
        industry.setLocation(location);
        map[y][x] = industry;
        industries.add(industry);
        return true;
    }

    public List<Industry> getIndustries() {
        return new ArrayList<>(industries);
    }

    // --- Cidades ---
    public boolean addCity(City city) {
        int x = city.getPositionX(), y = city.getPositionY();
        if (!isEmpty(x, y)) {
            return false;
        }
        city.assignHouseBlocksAutomatic();
        map[y][x] = city;
        return true;
    }

    public List<String> getCities() {
        List<String> cityNames = new ArrayList<>();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Object obj = map[y][x];
                if (obj instanceof City) {
                    cityNames.add(((City) obj).getName());
                }
            }
        }
        return cityNames;
    }

    public boolean isCityNameDuplicate(String cityName) {
        return getCities().stream()
                .anyMatch(n -> n.equalsIgnoreCase(cityName));
    }

    // --- Grid utilitários ---
    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }
        return map[y][x] == null;
    }

    public boolean validateCoordinates(List<Location> positions) {
        for (Location pos : positions) {
            if (pos.getX() < 0 || pos.getX() >= getWidth() ||
                    pos.getY() < 0 || pos.getY() >= getHeight()) {
                return false;
            }
        }
        return true;
    }

    public int getWidth() {
        return map[0].length;
    }

    public int getHeight() {
        return map.length;
    }

    public Object[][] getMapGrid() {
        Object[][] copy = new Object[getHeight()][getWidth()];
        for (int y = 0; y < getHeight(); y++) {
            System.arraycopy(map[y], 0, copy[y], 0, getWidth());
        }
        return copy;
    }

    public void setMapGrid(Object[][] grid) {
        if (grid == null ||
                grid.length != getHeight() ||
                grid[0].length != getWidth()) {
            throw new IllegalArgumentException("Grid inválido");
        }
        for (int y = 0; y < getHeight(); y++) {
            System.arraycopy(grid[y], 0, map[y], 0, getWidth());
        }
    }

    // --- Atualização do estado ---
    /**
     * 1) Indústrias produzem/transformam;
     * 2) Estações coletam carga de indústrias próximas;
     * 3) Locomotivas movem-se e interagem com estações.
     */
    public void update() {
        for (Industry ind : industries) {
            ind.produceCargo();
        }
        for (Station st : stations) {
            st.collectNearbyIndustries(industries);
        }
        for (Locomotive loco : locomotives) {
            loco.move();
            if (loco.hasStationNearby()) {
                // TODO: implementar coleta de estação
            }
        }
    }

    public void updateStations() {
        for (Station st : stations) {
            st.collectNearbyIndustries(industries);
        }
    }

    // --- Locomotivas ---
    public void addLocomotive(Locomotive loco) {
        locomotives.add(loco);
    }

    public List<Locomotive> getLocomotives() {
        return new ArrayList<>(locomotives);
    }

    // --- Getters/Setters ---
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
