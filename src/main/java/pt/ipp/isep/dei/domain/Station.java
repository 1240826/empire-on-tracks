// Station.java
package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a station on the map capable of collecting cargo from industries
 * that are within a square of side (2*range+1) centered on it.
 */
public class Station {
    private final String id;
    private final String name;
    private final Location location;
    private final int range;            // e.g., 4
    private final int capacity = 24;
    private List<Cargo> cargoes;
    private List<Building> buildings;

    /**
     * @param id       Unique station identifier
     * @param name     Display name
     * @param location Central grid position
     * @param range    Range in “cells” for each direction (x and y)
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
     * Iterates through the list of industries and tries to collect cargo from each one
     * that is within a square of range defined by 'range':
     * |dx| <= range and |dy| <= range.
     *
     * @param industries list of all industries on the map
     */
    public void collectNearbyIndustries(List<Industry> industries) {
        for (Industry industry : industries) {
            Location indLoc = industry.getLocation();
            int dx = Math.abs(location.getX() - indLoc.getX());
            int dy = Math.abs(location.getY() - indLoc.getY());
            if (dx <= range && dy <= range) {
                // 1) generates available cargo
                Cargo produced = industry.getCargo();
                if (produced == null || produced.getQuantity() <= 0) {
                    continue;
                }

                // 2) calculates available space in station for this cargo type
                double spaceLeft = capacityOfType(produced.getType()) - totalOfType(produced.getType());
                if (spaceLeft <= 0) {
                    continue;
                }

                // 3) collect the minimum of what's produced and available space
                double toCollect = Math.min(produced.getQuantity(), spaceLeft);

                // 4) add to station (update or create Cargo)
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

                // 5) remove collected quantity from industry
                industry.consumeCargo(toCollect);
            }
        }
    }

    /**
     * Returns a defensive copy of the cargo list.
     */
    public List<Cargo> getCargoes() {
        return cargoes;
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

    private double capacityOfType(CargoType type) {
        return 24.0;
    }

    /** Adds quantity to internal storage. */
    public void addCargo(CargoType type, double quantity) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.addQuantity(quantity);
                return;
            }
        }
        cargoes.add(new Cargo(type, quantity));
    }

    /** Removes quantity from internal storage. */
    public void removeCargo(CargoType type, double quantity) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.removeQuantity(quantity);
                return;
            }
        }
    }

    /** Returns available quantity of a cargo type in the station. */
    public double getCargoQuantity(CargoType type) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                return c.getQuantity();
            }
        }
        return 0.0;
    }

    /** Clears all stored cargo. */
    public void clearCargoes() {
        for (Cargo c : cargoes) {
            c.setQuantity(0.0);
        }
    }

    /**
     * Collects output from nearby industries and then distributes internal cargo
     * to inputs of TransformingIndustry and MixedIndustry within range.
     * @param industries list of all industries on the map
     * @return textual log of what was collected and distributed
     */
    public List<String> collectAndDistribute(List<Industry> industries) {
        List<String> events = new ArrayList<>();

        // 1) collect available output
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
                            "Station %s collected %.2f of %s from %s",
                            name, qty, out.getType(), ind.getIndustryName()
                    ));
                }
            }
        }

        // 2) distribute to consuming industries
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
                                    "Station %s delivered %.2f of %s to %s",
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
                                    "Station %s delivered %.2f of %s to %s",
                                    name, give, type, mi.getIndustryName()
                            ));
                        }
                    }
                }
            }
        }

        return events;
    }

    public double loadCargo(CargoType type, double requested) {
        Objects.requireNonNull(type, "type cannot be null");
        if (requested <= 0) return 0.0;
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                double available = c.getQuantity();
                double taken = Math.min(available, requested);
                c.removeQuantity(taken);
                return taken;
            }
        }
        return 0.0;
    }

    /**
     * Receives 'quantity' units of 'type' adding to internal storage.
     * @throws IllegalArgumentException if quantity is negative
     */
    public void unloadCargo(CargoType type, double quantity) {
        Objects.requireNonNull(type, "type cannot be null");
        if (quantity < 0) throw new IllegalArgumentException("quantity cannot be negative");
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.addQuantity(quantity);
                return;
            }
        }
        cargoes.add(new Cargo(type, quantity));
    }

    /**
     * Checks whether the station has an active building of the given type.
     * @param type the type of building (e.g., "TELEPHONE")
     * @return true if present and active, false otherwise
     */
    public boolean hasBuilding(String type) {
        if (buildings == null) return false;
        return buildings.stream()
                .anyMatch(b -> b.getType().equalsIgnoreCase(type) && b.isActive());
    }

    /**
     * Removes all buildings of the given type, regardless of their state.
     * Used for replacing buildings like TELEGRAPH with TELEPHONE.
     * @param type the type of building to remove
     */
    public void removeBuilding(String type) {
        if (buildings != null) {
            buildings.removeIf(b -> b.getType().equalsIgnoreCase(type));
        }
    }

    /**
     * Reactivates an existing building of the given type if it is inactive.
     * Optional helper in case inactive buildings are supported.
     * @param type the type of building to activate
     */
    public void activateBuilding(String type) {
        if (buildings != null) {
            buildings.stream()
                    .filter(b -> b.getType().equalsIgnoreCase(type) && !b.isActive())
                    .findFirst()
                    .ifPresent(b -> b.isActive());
        }
    }
}
