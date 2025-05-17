package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma locomotiva que percorre uma rota de estações,
 * coleta carga delas até encher sua capacidade.
 */
public class Locomotive {
    private final String id;
    private final String type;
    private final double price;
    private final boolean requiresElectrification;

    private final int capacity;            // capacidade total (unidades)
    private final List<Cargo> currentLoad; // carga atualmente embarcada

    private final List<Station> route;     // sequência de estações a visitar
    private int currentIndex = 0;          // índice da estação atual na rota

    public Locomotive(String id,
                      String type,
                      double price,
                      boolean requiresElectrification,
                      int capacity,
                      List<Station> route) {
        this.id                       = Objects.requireNonNull(id);
        this.type                     = Objects.requireNonNull(type);
        this.price                    = price;
        this.requiresElectrification  = requiresElectrification;
        this.capacity                 = capacity;
        this.currentLoad              = new ArrayList<>();
        this.route                    = new ArrayList<>(Objects.requireNonNull(route));
    }

    public String getId()                     { return id; }
    public String getType()                   { return type; }
    public double getPrice()                  { return price; }
    public boolean requiresElectrification()  { return requiresElectrification; }

    /** Avança para a próxima estação na rota (circular). */
    public void move() {
        if (!route.isEmpty()) {
            currentIndex = (currentIndex + 1) % route.size();
        }
    }

    /** A locomotiva está “na” estação atual. */
    public boolean hasStationNearby() {
        return !route.isEmpty();
    }

    /**
     * Coleta carga da estação atual até encher a locomotiva.
     * Remove a quantidade coletada da estação.
     */
    public void collectFromStation() {
        if (route.isEmpty()) {
            return;
        }
        Station station = route.get(currentIndex);
        for (Cargo stationCargo : station.getCargoes()) {
            double available = stationCargo.getQuantity();
            if (available <= 0) {
                continue;
            }
            double canTake = Math.min(available, spaceLeft());
            if (canTake <= 0) {
                break;
            }

            // remove da estação
            stationCargo.removeQuantity(canTake);

            // adiciona ao trem (acumula por tipo)
            boolean added = false;
            for (Cargo c : currentLoad) {
                if (c.getType() == stationCargo.getType()) {
                    c.addQuantity(canTake);
                    added = true;
                    break;
                }
            }
            if (!added) {
                currentLoad.add(new Cargo(stationCargo.getType(), canTake));
            }
        }
    }

    /** Quanto volume ainda cabe na locomotiva. */
    private double spaceLeft() {
        double used = currentLoad.stream()
                .mapToDouble(Cargo::getQuantity)
                .sum();
        return capacity - used;
    }

    /** Carga atualmente embarcada (cópia defensiva). */
    public List<Cargo> getCurrentLoad() {
        List<Cargo> copy = new ArrayList<>();
        for (Cargo c : currentLoad) {
            copy.add(new Cargo(c.getType(), c.getQuantity()));
        }
        return copy;
    }

    @Override
    public String toString() {
        return "Locomotive{id='" + id + '\'' +
                ", load=" + currentLoad +
                "/" + capacity + '}';
    }
}

