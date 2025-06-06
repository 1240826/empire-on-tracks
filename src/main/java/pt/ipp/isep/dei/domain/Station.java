package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma estação no mapa capaz de coletar e distribuir carga
 * de indústrias que estejam dentro de sua área de alcance.
 *
 * <p>Tem dois construtores:
 * <ul>
 *   <li>Sem orientação (para tipos centrados 3×3, 5×5, depot…): usa
 *       um alcance simétrico em todas as direções igual a 'range'.</li>
 *   <li>Com orientação (para 4×4): recebe também uma Orientation e
 *       ajusta internamente quatro ranges (norte, sul, leste, oeste):
 *       na direção apontada utiliza (range–1) e nas demais utiliza full ‘range’.</li>
 * </ul>
 */
public class Station {
    private final String id;
    private final String name;
    private final Location location;

    // alcance base e direcional
    private final int range;
    private final int northRange;
    private final int southRange;
    private final int eastRange;
    private final int westRange;

    private final int capacity = 240;
    private final List<Cargo> cargoes;
    private List<Building> buildings;

    // -- Construtor para tipos centrados (3x3, 5x5, etc) --
    public Station(String id, String name, Location location, int range) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.range = range;
        this.northRange = range;
        this.southRange = range;
        this.eastRange  = range;
        this.westRange  = range;

        this.cargoes = new ArrayList<>();
        this.buildings = new ArrayList<>();
        for (CargoType type : CargoType.values()) {
            this.cargoes.add(new Cargo(type, 0.0));
        }
    }

    // -- Construtor específico para estações 4x4 com orientação --
    public Station(String id, String name, Location location, StationType station, String orientation) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.range = station.getHeight();

        switch (orientation) {
            case "NE":
                this.northRange = range - 1;
                this.eastRange  = range - 1;
                this.southRange = range;
                this.westRange  = range;
                break;
            case "SE":
                this.southRange = range - 1;
                this.eastRange  = range - 1;
                this.northRange = range;
                this.westRange  = range;
                break;
            case "SW":
                this.southRange = range - 1;
                this.westRange  = range - 1;
                this.northRange = range;
                this.eastRange  = range;
                break;
            case "NW":
                this.northRange = range - 1;
                this.westRange  = range - 1;
                this.southRange = range;
                this.eastRange  = range;
                break;
            default:
                this.northRange = range;
                this.westRange  = range;
                this.southRange = range;
                this.eastRange  = range;
                break;
        }

        this.cargoes = new ArrayList<>();
        this.buildings = new ArrayList<>();
        for (CargoType type : CargoType.values()) {
            this.cargoes.add(new Cargo(type, 0.0));
        }
    }

    // --- Getters ---
    public String getId()               { return id; }
    public String getStationName()      { return name; }
    public Location getLocation()       { return location; }
    public List<Building> getBuildings(){ return buildings; }
    public List<Cargo> getCargoes()     { return new ArrayList<>(cargoes); }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    // --- Coleta de indústrias próximas ---
    /**
     * Itera pelas indústrias e coleta carga apenas se estiver
     * dentro do retângulo definido pelos quatro ranges:
     *   -westRange ≤ dx ≤ eastRange
     *   -southRange ≤ dy ≤ northRange
     */
    public void collectNearbyIndustries(List<Industry> industries) {
        for (Industry industry : industries) {
            Location indLoc = industry.getLocation();
            int dx = indLoc.getX() - location.getX();   // >0 = a leste
            int dy = indLoc.getY() - location.getY();   // >0 = ao norte

            if (dx < -westRange || dx > eastRange ||
                    dy < -southRange || dy > northRange) {
                continue;
            }

            Cargo produced = industry.getCargo();
            if (produced == null || produced.getQuantity() <= 0) {
                continue;
            }

            double spaceLeft = capacityOfType(produced.getType()) - totalOfType(produced.getType());
            if (spaceLeft <= 0) {
                continue;
            }

            double toCollect = Math.min(produced.getQuantity(), spaceLeft);
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
            industry.consumeCargo(toCollect);
        }
    }

    // Métodos utilitários de carga/descarga/distribuição

    public double loadCargo(CargoType type, double requested) {
        Objects.requireNonNull(type, "type não pode ser null");
        if (requested <= 0) return 0.0;
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                double taken = Math.min(c.getQuantity(), requested);
                c.removeQuantity(taken);
                return taken;
            }
        }
        return 0.0;
    }

    public void unloadCargo(CargoType type, double quantity) {
        Objects.requireNonNull(type, "type não pode ser null");
        if (quantity < 0) throw new IllegalArgumentException("quantity não pode ser negativo");
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.addQuantity(quantity);
                return;
            }
        }
        cargoes.add(new Cargo(type, quantity));
    }

    public double getCargoQuantity(CargoType type) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                return c.getQuantity();
            }
        }
        return 0.0;
    }

    public void clearCargoes() {
        for (Cargo c : cargoes) {
            c.setQuantity(0.0);
        }
    }

    public boolean hasBuilding(String type) {
        if (buildings == null) return false;
        return buildings.stream()
                .anyMatch(b -> b.getType().equalsIgnoreCase(type) && b.isActive());
    }

    public void removeBuilding(String type) {
        if (buildings != null) {
            buildings.removeIf(b -> b.getType().equalsIgnoreCase(type));
        }
    }
    @Override
    public String toString() {
        return name + " @ " + location +
                " [cargoes=" + cargoes.size() + "/" + capacity + "]";
    }

    // --- Helpers internos ---
    private double totalOfType(CargoType type) {
        return cargoes.stream()
                .filter(c -> c.getType() == type)
                .mapToDouble(Cargo::getQuantity)
                .sum();
    }

    private double capacityOfType(CargoType type) {
        return (double) capacity;
    }
    /**
     * Carrega o máximo possível de carga do tipo dado desta estação
     * para os vagões (carriages) do comboio especificado.
     * Retorna o total efetivamente carregado.
     */
    public int collectCargoOfType(CargoType ctype, Train train) {
        for (Cargo c : cargoes) {
            if (c.getType() == ctype && c.getQuantity() > 0) {
                int available = (int) c.getQuantity();
                int totalLoaded = 0;
                for (Carriage carr : train.getCarriages()) {
                    if (carr.getCargoType() == ctype && carr.getAvailableCapacity() > 0 && available > 0) {
                        int loaded = carr.loadCargo(available);
                        c.removeQuantity(loaded);
                        totalLoaded += loaded;
                        available -= loaded;
                        if (available <= 0) break;
                    }
                }
                return totalLoaded;
            }
        }
        return 0;
    }

    /**
     * Coleta automaticamente carga de indústrias próximas e distribui para indústrias consumidoras.
     * Gera logs de eventos das operações.
     */
    public List<String> collectAndDistribute(List<Industry> industries) {
        List<String> events = new ArrayList<>();
        for (Industry industry : industries) {
            Location indLoc = industry.getLocation();
            int dx = indLoc.getX() - location.getX();
            int dy = indLoc.getY() - location.getY();

            // Dentro do retângulo definido pelos quatro ranges?
            if (dx < -westRange || dx > eastRange ||
                    dy < -southRange || dy > northRange) {
                continue;
            }

            // 1. Coletar carga da indústria (output)
            Cargo produced = industry.getCargo();
            if (produced != null && produced.getQuantity() > 0) {
                double spaceLeft = capacityOfType(produced.getType()) - totalOfType(produced.getType());
                if (spaceLeft > 0) {
                    double toCollect = Math.min(produced.getQuantity(), spaceLeft);
                    // Adiciona direto no cargoes
                    for (Cargo c : cargoes) {
                        if (c.getType() == produced.getType()) {
                            c.addQuantity(toCollect);
                            break;
                        }
                    }
                    industry.consumeCargo(toCollect);
                    events.add(String.format("Station %s collected %.2f of %s from %s",
                            name, toCollect, produced.getType(), industry.getIndustryName()));
                }
            }

            // 2. Distribuir para Transforming/MixedIndustry
            if (industry instanceof TransformingIndustry) {
                TransformingIndustry ti = (TransformingIndustry) industry;
                CargoType type = ti.getInputCargoType();
                double needed = ti.getDemand() - ti.getInputCargoQuantity();
                if (needed > 0) {
                    double avail = 0.0;
                    for (Cargo c : cargoes) {
                        if (c.getType() == type) {
                            avail = c.getQuantity();
                            break;
                        }
                    }
                    double give = Math.min(needed, avail);
                    if (give > 0) {
                        for (Cargo c : cargoes) {
                            if (c.getType() == type) {
                                c.removeQuantity(give);
                                break;
                            }
                        }
                        ti.setInputCargoQuantity(ti.getInputCargoQuantity() + give);
                        events.add(String.format("Station %s delivered %.2f of %s to %s",
                                name, give, type, ti.getIndustryName()));
                    }
                }
            } else if (industry instanceof MixedIndustry) {
                MixedIndustry mi = (MixedIndustry) industry;
                CargoType type = mi.getInputCargoType();
                double needed = mi.getDemand() - mi.getInputCargoQuantity();
                if (needed > 0) {
                    double avail = 0.0;
                    for (Cargo c : cargoes) {
                        if (c.getType() == type) {
                            avail = c.getQuantity();
                            break;
                        }
                    }
                    double give = Math.min(needed, avail);
                    if (give > 0) {
                        for (Cargo c : cargoes) {
                            if (c.getType() == type) {
                                c.removeQuantity(give);
                                break;
                            }
                        }
                        mi.setInputCargoQuantity(mi.getInputCargoQuantity() + give);
                        events.add(String.format("Station %s delivered %.2f of %s to %s",
                                name, give, type, mi.getIndustryName()));
                    }
                }
            }
        }
        return events;
    }



}
