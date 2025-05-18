package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.List;

public class ListStationController {
    private final Map map;

    public ListStationController(Map map) {
        this.map = map;
    }

    public List<String> getAllStations() {
        return map.getAllNamesStation();
    }

    public Station getStationDetails(String stationName) {
        Station station = getStationByName(stationName);
        if (station != null) {
            return station;
        }
        return null;
    }

    private Station getStationByName(String name) {
        for (Station s : map.getStations()) {
            if (s.getStationName().equals(name)) {
                return s;
            }
        }
        return null;
    }
}
