package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a train, composed of a locomotive and carriages,
 * possibly carrying cargo and assigned to a route. Handles simulation of movement and cargo operations.
 */
public class Train {
    private String name;
    private Locomotive locomotive;
    private List<Carriage> carriages;
    private List<String> currentCargoes;
    private Route assignedRoute;

    /** Index in route for the current stop (station) the train is at. */
    private int currentStopIndex = 0;

    /** How many "simulation months" remain until this train reaches its next stop. */
    private double remainingTravelMonths = 0.0;

    /** The station the train is currently en route to (null if at station). */
    private Station enRouteTo = null;

    /**
     * Constructs a Train with the specified name, locomotive, carriages, and current cargoes.
     *
     * @param name           the name of the train (cannot be null)
     * @param locomotive     the locomotive associated with the train (cannot be null)
     * @param carriages      the list of carriages (cannot be null)
     * @param currentCargoes the list of current cargo types (cannot be null)
     */
    public Train(String name,
                 Locomotive locomotive,
                 List<Carriage> carriages,
                 List<String> currentCargoes) {
        this.name = Objects.requireNonNull(name);
        this.locomotive = Objects.requireNonNull(locomotive);
        this.carriages = Objects.requireNonNull(carriages);
        this.currentCargoes = Objects.requireNonNull(currentCargoes);
    }

    /**
     * Returns the name of the train.
     *
     * @return the train name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the locomotive.
     *
     * @return the locomotive type as a string, or an empty string if locomotive is null
     */
    public String getLocomotiveType() {
        return locomotive != null ? locomotive.getType() : "";
    }

    /**
     * Returns the list of current cargo types carried by the train.
     *
     * @return the list of current cargo types
     */
    public List<String> getCurrentCargoes() {
        return currentCargoes;
    }

    /**
     * Assigns a route to the train and resets the current stop index and travel state.
     * Re-creates carriages for the new route.
     *
     * @param route the route to assign (cannot be null)
     */
    public void assignRoute(Route route) {
        this.assignedRoute = Objects.requireNonNull(route);
        List<Carriage> newCarriages = new ArrayList<>();
        for (PointOfRoute por : route.getPointsOfRoute()) {
            for (CargoType cargoType : por.getCargoes()) {
                newCarriages.add(new Carriage(cargoType));
            }
        }
        this.carriages = newCarriages;
        this.currentStopIndex = 0;
        this.enRouteTo = null;
        this.remainingTravelMonths = 0.0;
    }

    /**
     * Returns the route assigned to the train.
     *
     * @return the assigned route, or null if none
     */
    public Route getAssignedRoute() {
        return assignedRoute;
    }

    /**
     * Returns true if the train is currently "in transit" (moving between stations).
     *
     * @return true if the train is traveling, false if at station
     */
    public boolean isInTransit() {
        return enRouteTo != null;
    }

    /**
     * Returns the next station in the route (circular), or null if no route.
     *
     * @return the next station in the route
     */
    public Station getNextStationOnRoute() {
        if (assignedRoute == null) return null;
        int n = assignedRoute.getNumberOfStops();
        int nextIndex = (currentStopIndex + 1) % n;
        return assignedRoute.getPointsOfRoute().get(nextIndex).getStation();
    }

    /**
     * Initiates travel to the specified station, setting the travel time based on total distance and locomotive speed.
     *
     * @param nextStation the next station to travel to
     * @param distanceKm  the total distance to travel (km)
     */
    public void startTravelTo(Station nextStation, double distanceKm) {
        this.enRouteTo = nextStation;
        // Calculate speed: locomotive top speed in km/h × 24 × 30 (days/month) = km/month
        double speedKmPerHr = locomotive.getTopSpeed();
        double speedKmPerMonth = speedKmPerHr * 24.0 * 30.0;
        double monthsNeeded = distanceKm / speedKmPerMonth;
        this.remainingTravelMonths = monthsNeeded;
    }

    /**
     * Advances the train's travel by one simulation month.
     * If in transit, decrements remaining travel months. When reaching the destination,
     * updates current stop index, clears enRouteTo, and returns true to indicate arrival.
     *
     * @return true if the train has just arrived at a stop, false otherwise
     */
    public boolean advanceTravelByOneMonth() {
        if (enRouteTo == null) return false; // Already at a station
        remainingTravelMonths -= 1.0;
        if (remainingTravelMonths <= 0.0) {
            // Arrived at destination: update stop index and travel state
            List<PointOfRoute> porList = assignedRoute.getPointsOfRoute();
            for (int i = 0; i < porList.size(); i++) {
                if (porList.get(i).getStation().equals(enRouteTo)) {
                    this.currentStopIndex = i;
                    break;
                }
            }
            enRouteTo = null;
            remainingTravelMonths = 0.0;
            return true;
        }
        return false;
    }

    /**
     * Gets the list of cargo types assigned to the train's current stop,
     * if the train is at a station (not in transit).
     *
     * @return list of cargo types to pick up at the current stop,
     *         or empty list if not at a station or no route
     */
    public List<CargoType> getAssignedCargoTypesForCurrentStop() {
        if (assignedRoute == null || enRouteTo != null) {
            return List.of();
        }
        return assignedRoute.getPointsOfRoute()
                .get(currentStopIndex)
                .getCargoes();
    }

    /**
     * Returns a string representation of the train, including its name,
     * locomotive type, and route information.
     *
     * @return a string representation of the train
     */
    @Override
    public String toString() {
        return String.format("Train[name=%s, loco=%s, route=%s]",
                name,
                locomotive.getType(),
                assignedRoute != null ? assignedRoute.toString() : "none");
    }

    /**
     * Returns true if this train currently has an assigned route.
     *
     * @return true if assignedRoute is not null
     */
    public boolean hasRoute() {
        return assignedRoute != null;
    }

    /**
     * Returns the current stop index (0-based) within the assigned route.
     * @return the index of the stop the train is at or just arrived to
     */
    public int getCurrentStopIndex() {
        return currentStopIndex;
    }

    /**
     * Returns the total available carriage capacity for the given cargo type.
     *
     * @param ctype The cargo type.
     * @return The total available space for that cargo type in all carriages.
     */
    public int getAvailableCapacityFor(CargoType ctype) {
        int total = 0;
        for (Carriage c : carriages) {
            if (c.getCargoType() == ctype) {
                total += c.getAvailableCapacity();
            }
        }
        return total;
    }

    /**
     * Loads up to the specified quantity of cargo into available carriages of the given type.
     * Returns the actual amount loaded.
     *
     * @param ctype The cargo type to load.
     * @param qty   The amount to load.
     * @return The amount actually loaded.
     */
    public int loadCargo(CargoType ctype, int qty) {
        int leftToLoad = qty;
        for (Carriage c : carriages) {
            if (c.getCargoType() == ctype && leftToLoad > 0) {
                int loaded = c.loadCargo(leftToLoad);
                leftToLoad -= loaded;
            }
        }
        return qty - leftToLoad; // amount actually loaded
    }

}

