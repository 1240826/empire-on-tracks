// Station.java
package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Station entity – supports three footprints (Depot 3×3, Terminal 5×5, Station 4×4).
 * Now includes <code>collectAndDistribute</code> (with event strings) so the
 * existing <code>SimulatorController</code> continues to work unchanged.
 */
public class Station {

    /*──────────────────── Identity & footprint ───────────────────*/
    private final String   id;
    private final String   name;
    private final Location location;
    private final StationType type;
    /** "CENTER" for Depot / Terminal, otherwise NE / NW / SE / SW. */
    private  String   anchorDirection="Center";

    /*──────────────────────── Stock & buildings ──────────────────*/
    private final int capacity = 24;                 // per cargo type
    private  List<Cargo> cargoes   = new ArrayList<>();
    private       List<Building> buildings = new ArrayList<>();

    /*───────────────────── Legacy square‑radius ──────────────────*/
    private int range;                               // kept only for backward calls

    /*────────────────────────── Constructors ─────────────────────*/
    public Station(String id, String name, Location loc, StationType type) {
        this(id, name, loc, type, "CENTER");
    }
    public Station(String id, String name, Location location, StationType type, String orientation) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
        this.range = type.getWidth();  // or adapt if range logic is separate
        this.cargoes = new ArrayList<>();
        this.buildings = new ArrayList<>();
        this.anchorDirection = orientation;
        for (CargoType cargoType : CargoType.values()) {
            this.cargoes.add(new Cargo(cargoType, 0.0));
        }
    }

    /*──────────────────────────── Getters ────────────────────────*/
    public String getId()            { return id; }
    public String getStationName()   { return name; }
    public Location getLocation()    { return location; }
    public StationType getType()     { return type; }
    public List<Cargo> getCargoes()  { return Collections.unmodifiableList(cargoes); }

    /*───────────────── Footprint helper (occupied cells) ─────────*/
    public List<Location> calculateOccupiedCells() {

        List<Location> occupied = new ArrayList<>();
        int x = location.getX();
        int y = location.getY();
        int width = type.getWidth();
        int height = type.getHeight();

        int startX, startY, endX, endY;

        switch (this.anchorDirection.toUpperCase()) {
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
                throw new IllegalArgumentException("Invalid anchor direction: " + this.anchorDirection);
        }

        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                occupied.add(new Location(i, j));
            }
        }

        return occupied;
    }
    /**
     * Returns the list of occupied cells (alias for calculateOccupiedCells, for backward compatibility).
     */
    public List<Location> footprint() {
        return calculateOccupiedCells();
    }

    /*──────────────── collectAndDistribute (used by Simulator) ──*/
    public List<String> collectAndDistribute(List<Industry> industries) {
        List<String> events = new ArrayList<>();
        List<Location> area = footprint();

        /* 1) Collect cargo produced inside footprint */
        for (Industry ind : industries) {
            if (!area.contains(ind.getLocation())) continue;
            Cargo produced = ind.getCargo();
            if (produced == null || produced.getQuantity() <= 0) continue;
            double free = capacity - getCargoQuantity(produced.getType());
            if (free <= 0) continue;
            double take = Math.min(produced.getQuantity(), free);
            addCargoInternal(produced.getType(), take);
            ind.consumeCargo(take);
            events.add(String.format("Station %s collected %.2f of %s from %s",
                    name, take, produced.getType(), ind.getIndustryName()));
        }

        /* 2) Deliver to industries that need input */
        for (Industry ind : industries) {
            if (!area.contains(ind.getLocation())) continue;

            if (ind instanceof TransformingIndustry ti) {
                CargoType type = ti.getInputCargoType();
                double need = ti.getDemand() - ti.getInputCargoQuantity();
                if (need > 0) {
                    double give = Math.min(need, getCargoQuantity(type));
                    if (give > 0) {
                        ti.setInputCargoQuantity(ti.getInputCargoQuantity() + give);
                        removeCargoInternal(type, give);
                        events.add(String.format("Station %s delivered %.2f of %s to %s",
                                name, give, type, ti.getIndustryName()));
                    }
                }
            } else if (ind instanceof MixedIndustry mi) {
                CargoType type = mi.getInputCargoType();
                double need = mi.getDemand() - mi.getInputCargoQuantity();
                if (need > 0) {
                    double give = Math.min(need, getCargoQuantity(type));
                    if (give > 0) {
                        mi.setInputCargoQuantity(mi.getInputCargoQuantity() + give);
                        removeCargoInternal(type, give);
                        events.add(String.format("Station %s delivered %.2f of %s to %s",
                                name, give, type, mi.getIndustryName()));
                    }
                }
            }
        }
        return events;
    }

    /*────────────────── Loco ⇄ Station I/O (API) ────────────────*/
    public double loadCargo(CargoType type, double requested) {
        if (requested <= 0) return 0;
        double avail = getCargoQuantity(type);
        double take  = Math.min(avail, requested);
        removeCargoInternal(type, take);
        return take;
    }
    public void unloadCargo(CargoType type, double qty) {
        if (qty > 0) addCargoInternal(type, qty);
    }

    /*─────────────────── Small helpers (cargo) ──────────────────*/
    private void addCargoInternal(CargoType t, double q) {
        cargoes.stream()
                .filter(c -> c.getType() == t)
                .findFirst()
                .ifPresentOrElse(c -> c.addQuantity(q), () -> cargoes.add(new Cargo(t, q)));
    }
    private void removeCargoInternal(CargoType t, double q) {
        cargoes.stream()
                .filter(c -> c.getType() == t)
                .findFirst()
                .ifPresent(c -> c.removeQuantity(q));
    }
    public double getCargoQuantity(CargoType t) {
        return cargoes.stream()
                .filter(c -> c.getType() == t)
                .mapToDouble(Cargo::getQuantity)
                .findFirst().orElse(0);
    }
    private double capacityOfType(CargoType type) {
        return 24.0;
    }
    private double totalOfType(CargoType type) {
        return cargoes.stream()
                .filter(c -> c.getType() == type)
                .mapToDouble(Cargo::getQuantity).sum();
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



    /*──────────────────── Debug toString ───────────────────────*/
    @Override public String toString() {
        return name + "@" + location + " cargo:" + cargoes.size();
    }

    public boolean hasBuilding(String type) {
        return false;
    }
    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void removeBuilding(String type) {
        buildings.removeIf(b -> b.getType().equalsIgnoreCase(type));
    }



    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }
}


