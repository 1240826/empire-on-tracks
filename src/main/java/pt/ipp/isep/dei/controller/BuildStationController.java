package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;

import java.util.List;
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


    /**
     * Finds the closest city to the given (x, y) coordinates.
     * Always returns the closest, regardless of distance.
     */
//    public City getClosestCity(int x, int y) {
//        List<City> cities = map.getCityObjects();  // Make sure Map has this method!
//        if (cities.isEmpty()) {
//            return null;
//        }
//        City closest = null;
//        double bestDist = Double.MAX_VALUE;
//        for (City c : cities) {
//            Location center = c.getCenter();
//            int dx = x - center.getX();
//            int dy = y - center.getY();
//            double dist = Math.sqrt(dx * dx + dy * dy);
//            if (dist < bestDist) {
//                bestDist = dist;
//                closest = c;
//            }
//        }
//        return closest;
//    }
//
//    /**
//     * Finds the closest station to the center of the given city.
//     */
//    public Station getClosestStationToCity(City city) {
//        List<Station> stations = map.getStations();
//        if (stations.isEmpty()) {
//            return null;
//        }
//        Station closest = null;
//        double bestDist = Double.MAX_VALUE;
//        Location cityCenter = city.getCenter();
//        for (Station s : stations) {
//            Location sp = s.getLocation();
//            int dx = sp.getX() - cityCenter.getX();
//            int dy = sp.getY() - cityCenter.getY();
//            double dist = Math.sqrt(dx * dx + dy * dy);
//            if (dist < bestDist) {
//                bestDist = dist;
//                closest = s;
//            }
//        }
//        return closest;
//    }
}
