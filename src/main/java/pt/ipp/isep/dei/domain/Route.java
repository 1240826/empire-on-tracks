// File: pt/ipp/isep/dei/domain/Route.java
package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController.StationCargo;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Representa uma rota como uma sequência ordenada de pares estação+carga (StationCargo).
 * A rota deve ter pelo menos duas paradas: inicial e final.
 */
public class Route {
    private final List<StationCargo> stops;

    /**
     * Constrói uma rota com a lista de paradas.
     * @param stops lista de StationCargo, deve ter tamanho >= 2
     * @throws IllegalArgumentException se stops for nulo ou tiver menos de 2 elementos
     */
    public Route(List<StationCargo> stops) {
        Objects.requireNonNull(stops, "stops não pode ser null");
        if (stops.size() < 2) {
            throw new IllegalArgumentException("A rota deve ter pelo menos estação inicial e final");
        }
        this.stops = List.copyOf(stops);
    }

    /** Retorna os pares estação+carga desta rota (imutável). */
    public List<StationCargo> getStops() {
        return Collections.unmodifiableList(stops);
    }

    /** Primeira parada (estação + carga). */
    public StationCargo getInitialStop() {
        return stops.get(0);
    }

    /** Última parada (estação + carga). */
    public StationCargo getFinalStop() {
        return stops.get(stops.size() - 1);
    }

    /** Sequência de tipos de carga transportados, na ordem das paradas. */
    public List<CargoType> getCargoTypes() {
        return stops.stream()
                .map(StationCargo::getCargoType)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return stops.stream()
                .map(Object::toString)
                .collect(Collectors.joining(" -> "));
    }
}
