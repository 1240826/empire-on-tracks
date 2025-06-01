// File: pt/ipp.isep.dei.domain/Train.java
package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a train, composed of a locomotive and carriages,
 * possibly carrying cargo and assigned to a route.
 */
public class Train {
    private String name;
    private Locomotive locomotive;
    private List<Carriage> carriages;
    private List<String> currentCargoes;
    private Route assignedRoute;

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
     * Assigns a route to the train and resets the current stop index.
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

        int currentStopIndex = 0;
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

}
