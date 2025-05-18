package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.Objects;

public class BuildStationController {
    private final Map map;

    public BuildStationController(Map map) {
        this.map = Objects.requireNonNull(map);
    }

    public boolean buildStation(String name, int x, int y, StationType type) {
        if (name == null || name.isBlank()) return false;

        // Verifica se a área definida pelo tipo está livre
        int width = type.getWidth();
        int height = type.getHeight();
        for (int dy = 0; dy < height; dy++) {
            for (int dx = 0; dx < width; dx++) {
                if (!map.isEmpty(x + dx, y + dy)) return false;
            }
        }

        Location location = new Location(x, y);
        Station station = new Station("ST" + map.getStations().size(), name, location, type.getNumTracks());
        map.addStation(station);
        return true;
    }
}
