package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the simulation map containing stations, industries,
 * rail lines, cities, and locomotives on a 2D grid.
 * <p>
 * Provides methods to add and retrieve elements, validate coordinates,
 * compute shortest-path distances, update state, and collect events during simulation steps.
 * </p>
 */
public class Map {
    /** Unique name of the map. */
    private String name;

    /** 2D grid holding objects placed on the map. */
    private final Object[][] map;

    /** Collection of rail lines on the map. */
    private final List<RailLine> railLines     = new ArrayList<>();

    /** Collection of stations on the map. */
    private final List<Station> stations       = new ArrayList<>();

    /** Collection of industries on the map. */
    private final List<Industry> industries    = new ArrayList<>();

    /** Collection of locomotives on the map. */
    private final List<Locomotive> locomotives = new ArrayList<>();

    /** Optional scenario for applying dynamic events to the map. */
    private Scenario scenario;

    /**
     * Default constructor.
     * <p>
     * Creates a map named "name" with default dimensions 10×10.
     * </p>
     */
    public Map() {
        this("name", 10, 10);
    }

    /**
     * Constructs a map with the given name and dimensions.
     *
     * @param name   the name of the map
     * @param width  the width of the grid (number of columns)
     * @param height the height of the grid (number of rows)
     */
    public Map(String name, int width, int height) {
        this.name = name;
        this.map  = new Object[height][width];
    }

    // --- Stations ---

    /**
     * Adds a station to the map.
     *
     * @param station the {@link Station} to add
     */
    public void addStation(Station station) {
        stations.add(station);
    }

    /**
     * Retrieves a list of all stations on the map.
     *
     * @return a new {@code List} containing all stations
     */
    public List<Station> getStations() {
        return stations;
    }

    /**
     * Retrieves the names of all stations on the map.
     *
     * @return a {@code List} of station names
     */
    public List<String> getAllNamesStation() {
        List<String> names = new ArrayList<>();
        for (Station s : stations) {
            names.add(s.getStationName());
        }
        return names;
    }

    /**
     * Finds and returns the station with the given name.
     *
     * @param stationName the name of the station to look up
     * @return the {@link Station} if found, or {@code null} otherwise
     */
    public Station getDetails(String stationName) {
        for (Station s : stations) {
            if (s.getStationName().equals(stationName)) {
                return s;
            }
        }
        return null;
    }

    // --- Rail Lines ---

    /**
     * Adds a rail line to the map.
     *
     * @param line the {@link RailLine} to add
     */
    public void addRailLine(RailLine line) {
        railLines.add(line);
    }

    /**
     * Retrieves all rail lines on the map.
     *
     * @return a new {@code List} containing all rail lines
     */
    public List<RailLine> getRailLines() {
        return new ArrayList<>(railLines);
    }

    // --- Industries ---

    /**
     * Places an industry at the specified location on the grid.
     *
     * @param industry the {@link Industry} to place
     * @param location the {@link Location} where to place the industry
     * @return {@code true} if placement succeeded; {@code false} if the cell is occupied or out of bounds
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

    /**
     * Retrieves all industries on the map.
     *
     * @return a new {@code List} containing all industries
     */
    public List<Industry> getIndustries() {
        return new ArrayList<>(industries);
    }

    // --- Cities ---

    /**
     * Adds a city to the map at its designated coordinates.
     * <p>
     * Automatically assigns house blocks to the city.
     * </p>
     *
     * @param city the {@link City} to add
     * @return {@code true} if placement succeeded; {@code false} otherwise
     */
    public boolean addCity(City city) {
        int x = city.getCenterX(), y = city.getCenterY();
        if (!isEmpty(x, y)) {
            System.out.println("Overbuilding");
            return false;
        }
        city.assignHouseBlocksAutomatic();
        map[y][x] = city;
        return true;
    }

    /**
     * Retrieves the names of all cities placed on the map.
     *
     * @return a {@code List} of city names
     */
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

    /**
     * Checks if a city name already exists on the map (case-insensitive).
     *
     * @param cityName the name to check
     * @return {@code true} if a city with the same name exists; {@code false} otherwise
     */
    public boolean isCityNameDuplicate(String cityName) {
        return getCities().stream()
                .anyMatch(n -> n.equalsIgnoreCase(cityName));
    }

    // --- Grid Utilities ---

    /**
     * Checks if a specific cell is within bounds and not occupied.
     *
     * @param x the x-coordinate (column)
     * @param y the y-coordinate (row)
     * @return {@code true} if the cell is empty and within the grid; {@code false} otherwise
     */
    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }
        return map[y][x] == null;
    }

    /**
     * Validates that all provided locations are within the grid bounds.
     *
     * @param positions a list of {@link Location} objects to validate
     * @return {@code true} if all locations are valid; {@code false} otherwise
     */
    public boolean validateCoordinates(List<Location> positions) {
        for (Location pos : positions) {
            if (pos.getX() < 0 || pos.getX() >= getWidth() ||
                    pos.getY() < 0 || pos.getY() >= getHeight()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the width (number of columns) of the map grid.
     *
     * @return the width of the grid
     */
    public int getWidth() {
        return map[0].length;
    }

    /**
     * Gets the height (number of rows) of the map grid.
     *
     * @return the height of the grid
     */
    public int getHeight() {
        return map.length;
    }

    /**
     * Returns a deep copy of the map grid.
     *
     * @return a two-dimensional {@code Object} array copy of the grid
     */
    public Object[][] getMapGrid() {
        Object[][] copy = new Object[getHeight()][getWidth()];
        for (int y = 0; y < getHeight(); y++) {
            System.arraycopy(map[y], 0, copy[y], 0, getWidth());
        }
        return copy;
    }

    /**
     * Replaces the internal map grid with the provided one after validation.
     *
     * @param grid a two-dimensional {@code Object} array to set
     * @throws IllegalArgumentException if the provided grid is null or has incorrect dimensions
     */
    public void setMapGrid(Object[][] grid) {
        if (grid == null ||
                grid.length != getHeight() ||
                grid[0].length != getWidth()) {
            throw new IllegalArgumentException("Invalid grid");
        }
        for (int y = 0; y < getHeight(); y++) {
            System.arraycopy(grid[y], 0, map[y], 0, getWidth());
        }
    }

    // --- Distance Calculation ---

    /**
     * Computes the shortest total distance (in km) between two stations, using Dijkstra's algorithm.
     * Considers all possible paths (direct or with intermediate stations) over existing rail lines.
     * <p>
     * If no route exists, returns {@link Double#POSITIVE_INFINITY}.
     * </p>
     *
     * @param stationA Starting station
     * @param stationB Destination station
     * @return shortest distance (in km), or POSITIVE_INFINITY if unreachable
     */
    public double getShortestDistance(Station stationA, Station stationB) {
        // Use full-qualified names to avoid confusion with this class "Map"
        java.util.Map<Station, java.util.List<RailLine>> adjLines = new java.util.HashMap<>();
        for (RailLine line : this.railLines) {
            Station s1 = line.getStart();
            Station s2 = line.getEnd();
            adjLines.computeIfAbsent(s1, k -> new java.util.ArrayList<>()).add(line);
            adjLines.computeIfAbsent(s2, k -> new java.util.ArrayList<>()).add(line);
        }

        java.util.Map<Station, Double> dist = new java.util.HashMap<>();
        java.util.Set<Station> visited = new java.util.HashSet<>();
        for (Station s : this.stations) {
            dist.put(s, Double.POSITIVE_INFINITY);
        }
        dist.put(stationA, 0.0);

        java.util.PriorityQueue<Station> pq = new java.util.PriorityQueue<>(java.util.Comparator.comparing(dist::get));
        pq.add(stationA);

        while (!pq.isEmpty()) {
            Station cur = pq.poll();
            if (visited.contains(cur)) continue;
            visited.add(cur);

            if (cur.equals(stationB)) {
                return dist.get(cur);
            }

            java.util.List<RailLine> neighbors = adjLines.getOrDefault(cur, java.util.Collections.emptyList());
            for (RailLine line : neighbors) {
                Station nbr = (line.getStart().equals(cur)) ? line.getEnd() : line.getStart();
                if (visited.contains(nbr)) continue;

                double newDist = dist.get(cur) + line.getDistance();
                if (newDist < dist.get(nbr)) {
                    dist.put(nbr, newDist);
                    pq.add(nbr);
                }
            }
        }

        return Double.POSITIVE_INFINITY;
    }

    // --- State Updates ---

    /**
     * Performs a simulation update step:
     * <ol>
     *   <li>Industries produce or transform cargo.</li>
     *   <li>Stations collect cargo from nearby industries.</li>
     * </ol>
     */
    public void update() {
        for (Industry ind : industries) {
            ind.produceCargo();
        }
    }

    /**
     * Updates only the stations by collecting from nearby industries.
     */
    public void updateStations() {
        for (Station st : stations) {
            st.collectNearbyIndustries(industries);
        }
    }

    // --- Locomotives ---

    /**
     * Adds a locomotive to the map.
     *
     * @param loco the {@link Locomotive} to add
     */
    public void addLocomotive(Locomotive loco) {
        locomotives.add(loco);
    }

    /**
     * Retrieves all locomotives on the map.
     *
     * @return a new {@code List} containing all locomotives
     */
    public List<Locomotive> getLocomotives() {
        return new ArrayList<>(locomotives);
    }

    // --- Name Accessors ---

    /**
     * Returns the name of the map.
     *
     * @return the map’s name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the map.
     *
     * @param name the new name to assign
     */
    public void setName(String name) {
        this.name = name;
    }
}
