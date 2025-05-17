// Station.java
package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma estação no mapa capaz de coletar carga de indústrias
 * que estejam dentro de um quadrado de lado (2*range+1) centrado nela.
 */
public class Station {
    private final String id;
    private final String name;
    private final Location location;
    private final int range;            // ex: 4
    private final int capacity = 24;
    private  List<Cargo> cargoes;
    private  List<Building> buildings;

    /**
     * @param id       Identificador único da estação
     * @param name     Nome para exibição
     * @param location Posição central no grid
     * @param range    Alcance em “células” para cada direção (x e y)
     */
    public Station(String id, String name, Location location, int range) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.range = range;
        this.cargoes = new ArrayList<>();
        for (CargoType type : CargoType.values()) {
            this.cargoes.add(new Cargo(type, 0.0));
        }

    }

    public String getId() {
        return id;
    }

    public String getStationName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
    public List<Building> getBuildings() {
        return buildings;
    }
    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
    public void addBuilding(Building building) {
        this.buildings.add(building);
    }




    /**
     * Percorre a lista de indústrias e tenta coletar carga de cada uma
     * que esteja dentro de um quadrado de alcance definido por 'range':
     * |dx| <= range e |dy| <= range.
     *
     * @param industries lista de todas as indústrias do mapa
     */
    public void collectNearbyIndustries(List<Industry> industries) {
        for (Industry industry : industries) {
            Location indLoc = industry.getLocation();
            int dx = Math.abs(location.getX() - indLoc.getX());
            int dy = Math.abs(location.getY() - indLoc.getY());
            if (dx <= range && dy <= range) {
                // 1) gera a carga disponível
                Cargo produced = industry.getCargo();
                if (produced == null || produced.getQuantity() <= 0) {
                    continue;
                }

                // 2) calcula quanto cabe na estação deste tipo
                double spaceLeft = capacityOfType(produced.getType()) - totalOfType(produced.getType());
                // se não cabe nada, pula
                if (spaceLeft <= 0) {
                    continue;
                }

                // 3) quantidade a coletar é o mínimo entre produzido e espaço disponível
                double toCollect = Math.min(produced.getQuantity(), spaceLeft);

                // 4) adiciona na estação (atualiza ou cria Cargo)
                boolean found = false;
                for (Cargo c : cargoes) {
                    if (c.getType() == produced.getType()) {
                        c.addQuantity(toCollect);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    cargoes.add(new Cargo(produced.getType(), toCollect));
                }

                // 5) remove exatamente esse 'toCollect' da indústria
                industry.consumeCargo(toCollect);
            }
        }
    }

    /**
     * Retorna cópia defensiva da lista de cargas.
     */
    public List<Cargo> getCargoes() {
        List<Cargo> copy = new ArrayList<>();
        for (Cargo c : cargoes) {
            copy.add(new Cargo(c.getType(), c.getQuantity()));
        }
        return copy;
    }

    @Override
    public String toString() {
        return name + " @ " + location +
                " [cargoes=" + cargoes.size() + "/" + capacity + "]";
    }
    private double totalOfType(CargoType type) {
        return cargoes.stream()
                .filter(c -> c.getType() == type)
                .mapToDouble(Cargo::getQuantity)
                .sum();
    }

    // capacidade fixa por tipo (24 unidades, por exemplo)
    private double capacityOfType(CargoType type) {
        return 24.0;
    }
}
