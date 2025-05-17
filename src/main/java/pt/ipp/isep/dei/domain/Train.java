package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Represents a train, composed of a locomotive and carriages,
 * and possibly carrying cargoes and assigned to a route.
 */
public class Train {
    /** The unique name of the train, set by the player. */
    private String name;

    /** The locomotive providing traction for this train. */
    private Locomotive locomotive;

    /** The list of carriages attached to the train. */
    private List<Carriage> carriages;

    /** The list of cargoes currently carried by the train (by cargo name/type). */
    private List<String> currentCargoes;

    /** The route currently assigned to this train (if any). */
    private Route route;

    /**
     * Constructs a new Train.
     *
     * @param name           the unique name of the train
     * @param locomotive     the locomotive powering the train
     * @param carriages      the list of carriages attached to the train
     * @param currentCargoes the list of cargoes currently on the train
     */
    public Train(String name, Locomotive locomotive, List<Carriage> carriages, List<String> currentCargoes) {
        this.name = name;
        this.locomotive = locomotive;
        this.carriages = carriages;
        this.currentCargoes = currentCargoes;
    }

    /**
     * Returns the train's unique name.
     *
     * @return the name of the train
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the type of the locomotive powering the train.
     *
     * @return the locomotive type, or an empty string if none assigned
     */
    public String getLocomotiveType() {
        return locomotive != null ? locomotive.getType() : "";
    }

    /**
     * Returns the list of current cargoes carried by the train.
     *
     * @return a list of current cargoes
     */
    public List<String> getCurrentCargoes() {
        return currentCargoes;
    }

    /**
     * Assigns a route to this train (overwriting any previous route).
     *
     * @param route the route to assign to the train
     */
    public void assignRoute(Route route) {
        this.route = route;
    }

    /**
     * Returns the route currently assigned to this train.
     *
     * @return the assigned route, or null if none assigned
     */
    public Route getRoute() {
        return route;
    }
}
