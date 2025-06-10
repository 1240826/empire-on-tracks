package pt.ipp.isep.dei.domain;
import java.util.List;
import java.util.Objects;

//public class CityManager {
//
//    private final List<City> cities;
//
//    public CityManager(List<City> cities) {
//        this.cities = Objects.requireNonNull(cities);
//    }
//
//    public City getClosestCity(int x, int y) {
//        if (cities.isEmpty()) {
//            return null;
//        }
//        City closest = null;
//        double bestDist = Double.MAX_VALUE;
//        for (City c : cities) {
//            Location cp = c.getCenter();
//            int dx = x - cp.getX();
//            int dy = y - cp.getY();
//            double dist = Math.sqrt(dx * dx + dy * dy);
//            if (dist < bestDist) {
//                bestDist = dist;
//                closest = c;
//            }
//        }
//        return closest;
//    }
//
//    public List<City> getCities() {
//        return List.copyOf(cities);
//    }
//}
