package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;

public class Station {
    private String id;
    private String name;
    private String type;
    private int range;
    private double cost;
    private List<Building> buildings = new ArrayList<>();
    private List<Cargo> cargoes = new ArrayList<>();

    public Station(String id, String name, String type, int range, double cost) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.range = range;
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public String getStationName() {
        return name;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public List<Cargo> getCargoes() {
        return cargoes;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public void addCargo(Cargo cargo) {
        cargoes.add(cargo);
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}