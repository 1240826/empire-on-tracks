package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.controller.BuildLineController;

public class RailLine {
    private Station start;
    private Station end;
    private boolean isElectrified;
    private boolean isDouble;
    private double cost;
    private double distance;
    //Constructor for RailLine
    public RailLine(Station start, Station end, boolean isElectrified, boolean isDouble, double distance) {
        this.start = start;
        this.end = end;
        this.isElectrified = isElectrified;
        this.isDouble = isDouble;
        this.cost = calculateCost();
        this.distance = distance;
    }

    //Calculates the cost of the RailLine
    private double calculateCost() {
        // Temos de ver o que colocar aqui
        double base = 6;
        if (isElectrified) base += 5;
        if (isDouble) base += 3;
        return base;
    }
    //Defines starting station
    public Station getStart() {
        return start;
    }
    //Defines second station
    public Station getEnd() {
        return end;
    }

    public boolean isElectrified() {
        return isElectrified;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public double getCost() {
        return cost;
    }

    public double getDistance() { return distance; }
}
