package pt.ipp.isep.dei.domain;

import java.util.List;

public class Route {
    private final List<?> stationCargoList;

    public Route(List<?> stationCargoList) {
        this.stationCargoList = stationCargoList;
    }

    public List<?> getStationCargoList() {
        return stationCargoList;
    }
}


