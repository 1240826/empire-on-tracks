package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {
    private String name;
    private Object[][] map;
    private List<RailLine> railLines = new ArrayList<>();
    private List<Station> stations = new ArrayList<>();

    public void addStation(Station station) {
        stations.add(station);
    }

    public List<String> getAllNamesStation() {
        List<String> names = new ArrayList<>();
        for (Station s : stations) {
            names.add(s.getStationName());
        }
        return names;
    }

    // returns details of station
    public Station getDetails(String stationName) {
        for (Station s : stations) {
            if (s.getStationName().equals(stationName)) {
                return s;
            }
        }
        return null;
    }

    public void addRailLine(RailLine line) {
        railLines.add(line);
    }

    public List<RailLine> getRailLines() {
        return new ArrayList<>(railLines);
    }

    // returns list of all stations
    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (map[y][x] instanceof Station) {
                    stations.add((Station) map[y][x]);
                }
            }
        }
        return stations;
    }

    public Map() {
        this.name = "name";
        map = new Object[10][10];
    }

    public Map(String name, int width, int height) {
        this.name = name;
        map = new Object[height][width];
    }


    public String getName() {
        return new String(name);
    }

    public void setName(String name) {
        this.name = new String(name);
    }

    public Object[][] getMap() {
        Object[][] copy = new Object[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                copy[i][j] = this.map[i][j];
            }
        }
        return copy;
    }

    public void setMap(Object[][] p) {
        Object[][] copy = new Object[p.length][p[0].length];
        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < p[0].length; j++) {
                copy[i][j] = p[i][j];
            }
        }
        this.map = copy;
    }

    public int getHeight() {
        return map.length;
    }

    public int getWidth() {
        return map[0].length;
    }

    public List<String> getCities() {
        List<String> cityNames = new ArrayList<>();
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                Object obj = map[y][x];
                if (obj instanceof City) {
                    City city = (City) obj;
                    cityNames.add(city.getName());
                }
            }
        }
        return cityNames;
    }

    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }
        return map[y][x] == null;
    }

    public boolean validateCoordinates(List<Location> positions) {
        for (Location pos : positions) {
            int x = pos.getX();
            int y = pos.getY();
            if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
                return false;
            }
        }
        return true;
    }

    public boolean addIndustry(Industry industry) {
        Location location = industry.getLocation();
        int x = location.getX();
        int y = location.getY();
        if (isEmpty(x, y)) {
            map[y][x] = industry;
            return true;
        }
        return false;
    }

    public boolean addCity(City city) {
        int x = city.getPositionX(), y = city.getPositionY();
        if (!isEmpty(x, y)) return false;
        city.assignHouseBlocksAutomatic();
        map[y][x] = city;
        return true;
    }
    /** Verifica se já existe cidade com este nome no mapa */
    public boolean isCityNameDuplicate(String cityName) {
        return getCities().stream()
                .anyMatch(name -> name.equalsIgnoreCase(cityName));
    }

    public boolean isCityNameDuplicate(String name) {
        return false;
    }

    public boolean validateCoordinates(List<Location> positions) {
        return false;
    }

    public boolean validatePosition(int newX, int newY) {
        return false;
    }
}



