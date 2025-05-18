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
    // --- helpers de estoque ---
    /** Adiciona quantidade ao estoque interno. */
    public void addCargo(CargoType type, double quantity) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.addQuantity(quantity);
                return;
            }
        }
        // caso não exista (não deve acontecer), adiciona novo
        cargoes.add(new Cargo(type, quantity));
    }

    /** Remove quantidade do estoque interno. */
    public void removeCargo(CargoType type, double quantity) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.removeQuantity(quantity);
                return;
            }
        }
    }

    /** Retorna quantidade disponível de um tipo de carga na estação. */
    public double getCargoQuantity(CargoType type) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                return c.getQuantity();
            }
        }
        return 0.0;
    }

    /** Zera todo o estoque da estação. */
    public void clearCargoes() {
        for (Cargo c : cargoes) {
            c.setQuantity(0.0);
        }
    }

// --- coleta + distribuição ---
    /**
     * Coleta outputs de indústrias próximas e em seguida distribui do seu estoque
     * para os inputs de TransformingIndustry e MixedIndustry dentro do mesmo alcance.
     * @param industries lista de todas indústrias no mapa
     * @return eventos textuais do que foi coletado e distribuído
     */
    public List<String> collectAndDistribute(List<Industry> industries) {
        List<String> events = new ArrayList<>();

        // 1) coleta todos os outputs disponíveis
        for (Industry ind : industries) {
            Location indLoc = ind.getLocation();
            int dx = Math.abs(indLoc.getX() - this.location.getX());
            int dy = Math.abs(indLoc.getY() - this.location.getY());
            if (dx <= range && dy <= range) {
                Cargo out = ind.getCargo();
                double qty = out.getQuantity();
                if (qty > 0) {
                    out.removeQuantity(qty);
                    addCargo(out.getType(), qty);
                    events.add(String.format(
                            "Estação %s coletou %.2f de %s de %s",
                            name, qty, out.getType(), ind.getIndustryName()
                    ));
                }
            }
        }

        // 2) distribui para indústrias consumidoras
        for (Industry ind : industries) {
            Location indLoc = ind.getLocation();
            int dx = Math.abs(indLoc.getX() - this.location.getX());
            int dy = Math.abs(indLoc.getY() - this.location.getY());
            if (dx <= range && dy <= range) {
                // TransformingIndustry
                if (ind instanceof TransformingIndustry) {
                    TransformingIndustry ti = (TransformingIndustry) ind;
                    CargoType type = ti.getInputCargoType();
                    double needed = ti.getDemand() - ti.getInputCargoQuantity();
                    if (needed > 0) {
                        double avail = getCargoQuantity(type);
                        double give = Math.min(needed, avail);
                        if (give > 0) {
                            ti.setInputCargoQuantity(ti.getInputCargoQuantity() + give);
                            removeCargo(type, give);
                            events.add(String.format(
                                    "Estação %s alimentou %.2f de %s para %s",
                                    name, give, type, ti.getIndustryName()
                            ));
                        }
                    }
                }
                // MixedIndustry
                else if (ind instanceof MixedIndustry) {
                    MixedIndustry mi = (MixedIndustry) ind;
                    CargoType type = mi.getInputCargoType();
                    double needed = mi.getDemand() - mi.getInputCargoQuantity();
                    if (needed > 0) {
                        double avail = getCargoQuantity(type);
                        double give = Math.min(needed, avail);
                        if (give > 0) {
                            mi.setInputCargoQuantity(mi.getInputCargoQuantity() + give);
                            removeCargo(type, give);
                            events.add(String.format(
                                    "Estação %s alimentou %.2f de %s para %s",
                                    name, give, type, mi.getIndustryName()
                            ));
                        }
                    }
                }
            }
        }

        return events;
    }

}
