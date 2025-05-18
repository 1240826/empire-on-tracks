// File: pt/ipp/isep/dei/domain/Train.java
package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.Objects;

/**
 * Representa um trem, composto por locomotiva e carruagens,
 * e possivelmente carregando cargas e atribuído a uma rota.
 */
public class Train {
    private String name;
    private Locomotive locomotive;
    private List<Carriage> carriages;
    private List<String> currentCargoes;
    private Route route;

    public Train(String name,
                 Locomotive locomotive,
                 List<Carriage> carriages,
                 List<String> currentCargoes) {
        this.name = Objects.requireNonNull(name);
        this.locomotive = Objects.requireNonNull(locomotive);
        this.carriages = Objects.requireNonNull(carriages);
        this.currentCargoes = Objects.requireNonNull(currentCargoes);
    }

    public String getName() {
        return name;
    }

    public String getLocomotiveType() {
        return locomotive != null ? locomotive.getType() : "";
    }

    public List<String> getCurrentCargoes() {
        return currentCargoes;
    }

    public void assignRoute(Route route) {
        this.route = Objects.requireNonNull(route);
    }

    public Route getRoute() {
        return route;
    }

    @Override
    public String toString() {
        return String.format("Train[name=%s, loco=%s, route=%s]",
                name,
                locomotive.getType(),
                route != null ? route.toString() : "none");
    }
}
