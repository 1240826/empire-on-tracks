package pt.ipp.isep.dei.domain;

import java.util.Arrays;
import java.util.List;

public class Map {
    private String name;
    private Object[][] map;

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
        return null;
    }

    public boolean isEmpty(int x, int y) {
        if (x < 0 || x >= getWidth() || y < 0 || y >= getHeight()) {
            return false;
        }
        return map[y][x] == null;
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
}
