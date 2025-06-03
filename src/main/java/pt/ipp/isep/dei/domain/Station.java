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
    private int range;                  // REMOVE final
    private final int capacity = 24;
    private final StationType type;     // ADDED
    private List<Cargo> cargoes;
    private List<Building> buildings;
    private String orientation="Center";

    /**
     * Legacy constructor (kept for compatibility).
     */
    public Station(String id, String name, Location location, int range) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.range = range;
        this.type = StationType.Station_4x4;  // default type if needed
        this.cargoes = new ArrayList<>();
        this.buildings = new ArrayList<>();
        for (CargoType cargoType : CargoType.values()) {
            this.cargoes.add(new Cargo(cargoType, 0.0));
        }
    }

    /**
     * New constructor using StationType.
     */
    public Station(String id, String name, Location location, StationType type, String orientation) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.range = type.getWidth();  // or adapt if range logic is separate
        this.cargoes = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.orientation = orientation;
        for (CargoType cargoType : CargoType.values()) {
            this.cargoes.add(new Cargo(cargoType, 0.0));
        }
    }

    public String getId() { return id; }

    public String getStationName() { return name; }

    public Location getLocation() { return location; }

    public StationType getType() { return type; }

    public List<Building> getBuildings() { return buildings; }

    public void setBuildings(List<Building> buildings) { this.buildings = buildings; }

    public void addBuilding(Building building) { this.buildings.add(building); }

    public int getRange() { return range; }  // getter if needed

    // NEW: Calculate occupied cells based on anchor direction
    public List<Location> calculateOccupiedCells() {

        List<Location> occupied = new ArrayList<>();
        int x = location.getX();
        int y = location.getY();
        int width = type.getWidth();
        int height = type.getHeight();

        int startX, startY, endX, endY;

        switch (this.orientation.toUpperCase()) {
            case "NE":
                startX = x - width + 1;
                endX = x;
                startY = y;
                endY = y + height - 1;
                break;
            case "SE":
                startX = x - width + 1;
                endX = x;
                startY = y - height + 1;
                endY = y;
                break;
            case "NW":
                startX = x;
                endX = x + width - 1;
                startY = y;
                endY = y + height - 1;
                break;
            case "SW":
                startX = x;
                endX = x + width - 1;
                startY = y - height + 1;
                endY = y;
                break;
            default:
                throw new IllegalArgumentException("Invalid anchor direction: " + this.orientation);
        }

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                occupied.add(new Location(i, j));
            }
        }

        return occupied;
    }

    public void collectNearbyIndustries(List<Industry> industries) {

        List<Location> occupiedCells = calculateOccupiedCells();

        for (Industry industry : industries) {
            Location indLoc = industry.getLocation();
            boolean isInside = occupiedCells.stream()
                    .anyMatch(cell -> cell.getX() == indLoc.getX() && cell.getY() == indLoc.getY());

            if (isInside) {
                Cargo produced = industry.getCargo();
                if (produced == null || produced.getQuantity() <= 0) continue;

                double spaceLeft = capacityOfType(produced.getType()) - totalOfType(produced.getType());
                if (spaceLeft <= 0) continue;

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
    }

    public List<Cargo> getCargoes() { return cargoes; }

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

    public void addCargo(CargoType type, double quantity) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.addQuantity(quantity);
                return;
            }
        }
        cargoes.add(new Cargo(type, quantity));
    }

    public void removeCargo(CargoType type, double quantity) {
        for (Cargo c : cargoes) {
            if (c.getType() == type) {
                c.removeQuantity(quantity);
                return;
            }
        }
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

    public List<String> collectAndDistribute(List<Industry> industries) {
        List<String> events = new ArrayList<>();

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

        for (Industry ind : industries) {
            Location indLoc = ind.getLocation();
            int dx = Math.abs(indLoc.getX() - this.location.getX());
            int dy = Math.abs(indLoc.getY() - this.location.getY());
            if (dx <= range && dy <= range) {
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

    public void activateBuilding(String type) {
        if (buildings != null) {
            buildings.stream()
                    .filter(b -> b.getType().equalsIgnoreCase(type) && !b.isActive())
                    .findFirst()
                    .ifPresent(b -> b.isActive());
        }
    }
}

