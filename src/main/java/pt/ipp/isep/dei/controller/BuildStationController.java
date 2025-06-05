package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.Objects;

public class BuildStationController {
    private final Map map;

    public BuildStationController(Map map) {
        this.map = Objects.requireNonNull(map);
    }

    public boolean buildStation(String name, int x, int y, StationType type, String Orientarion) {
        if (name == null || name.isBlank()) return false;

        // Verifica se a área definida pelo tipo está livre
        int width = type.getWidth();
        int height = type.getHeight();


        Location location = new Location(x, y);
        Station station = new Station("ST" + map.getStations().size(), name, location, type, Orientarion);
        map.addStation(station);
        return true;
    }
}
