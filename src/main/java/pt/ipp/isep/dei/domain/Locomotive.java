package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa uma locomotiva que "teletransporta"
 * diretamente entre estações, com capacidade limitada de carga.
 */
public class Locomotive {
    // Identificação e especificações
    private final String id;
    private final String type;                  // "Steam", "Diesel" ou "Electric"
    private final double price;
    private final boolean requiresElectrification;
    private final int power;
    private final double acceleration;
    private final double topSpeed;
    private final int startYear;
    private final double fuelCostPerYear;
    private final double maintenancePerYear;

    // Roteamento e carga
    private final List<Station> route;
    private int currentIndex;
    private final double capacity;
    private final List<Cargo> currentLoad = new ArrayList<>();

    /**
     * Construtor completo.
     */
    public Locomotive(String id,
                      String type,
                      double price,
                      boolean requiresElectrification,
                      int power,
                      double acceleration,
                      double topSpeed,
                      int startYear,
                      double fuelCostPerYear,
                      double maintenancePerYear,
                      double capacity,
                      List<Station> route) {
        this.id                      = Objects.requireNonNull(id);
        this.type                    = Objects.requireNonNull(type);
        this.price                   = price;
        this.requiresElectrification = requiresElectrification;
        this.power                   = power;
        this.acceleration            = acceleration;
        this.topSpeed                = topSpeed;
        this.startYear               = startYear;
        this.fuelCostPerYear         = fuelCostPerYear;
        this.maintenancePerYear      = maintenancePerYear;
        this.capacity                = capacity;
        this.route                   = new ArrayList<>(Objects.requireNonNull(route));
        this.currentIndex            = route.isEmpty() ? -1 : 0;
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getType() { return type; }
    public double getPrice() { return price; }
    public boolean requiresElectrification() { return requiresElectrification; }
    public int getPower() { return power; }
    public double getAcceleration() { return acceleration; }
    public double getTopSpeed() { return topSpeed; }
    public int getStartYear() { return startYear; }
    public double getFuelCostPerYear() { return fuelCostPerYear; }
    public double getMaintenancePerYear() { return maintenancePerYear; }
    public double getCapacity() { return capacity; }
    public List<Station> getRoute() { return new ArrayList<>(route); }
    public List<Cargo> getCurrentLoad() {
        List<Cargo> copy = new ArrayList<>();
        for (Cargo c : currentLoad) {
            copy.add(new Cargo(c.getType(), c.getQuantity()));
        }
        return copy;
    }

    // --- Movimento (teletransporte) ---
    /**
     * Teletransporta para a próxima estação na rota.
     * Se em rota circular, volta ao início ao final.
     */
    public void move() {
        if (route.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex + 1) % route.size();
    }

    /**
     * Retorna true se a locomotiva estiver em uma estação (ou seja, rota não vazia).
     */
    public boolean hasStationNearby() {
        return currentIndex >= 0 && !route.isEmpty();
    }

    /**
     * Retorna a estação atual, ou null se não tiver sido posicionada.
     */
    public Station getCurrentStation() {
        if (!hasStationNearby()) {
            return null;
        }
        return route.get(currentIndex);
    }

    // --- Coleta e descarga de carga ---
    /**
     * Coleta até encher a capacidade a partir do estoque da estação atual.
     * @return total carregado
     */
    public double collectFromStation() {
        Station st = getCurrentStation();
        if (st == null) {
            return 0;
        }
        double total = 0;
        // itera sobre as cargas disponíveis na estação
        for (Cargo stCargo : st.getCargoes()) {
            double available = stCargo.getQuantity();
            if (available <= 0) {
                continue;
            }
            double toTake = Math.min(available, spaceLeft());
            if (toTake <= 0) {
                break; // já está cheio
            }
            // remove da estação
            stCargo.removeQuantity(toTake);
            // adiciona na locomotiva
            boolean added = false;
            for (Cargo locoCargo : currentLoad) {
                if (locoCargo.getType() == stCargo.getType()) {
                    locoCargo.addQuantity(toTake);
                    added = true;
                    break;
                }
            }
            if (!added) {
                currentLoad.add(new Cargo(stCargo.getType(), toTake));
            }
            total += toTake;
        }
        return total;
    }

    /**
     * Descarrega toda a carga na estação atual.
     * @return total descarregado
     */
    public double unloadAllToCurrentStation() {
        Station st = getCurrentStation();
        if (st == null) {
            return 0;
        }
        double total = 0;
        for (Cargo c : new ArrayList<>(currentLoad)) {
            st.addCargo(c.getType(), c.getQuantity());
            total += c.getQuantity();
        }
        currentLoad.clear();
        return total;
    }

    /**
     * Espaço livre restante na locomotiva.
     */
    private double spaceLeft() {
        double used = currentLoad.stream()
                .mapToDouble(Cargo::getQuantity)
                .sum();
        return capacity - used;
    }

    @Override
    public String toString() {
        Station st = getCurrentStation();
        String loc = (st == null ? "n/a" : st.getStationName());
        return String.format(
                "Locomotive[id=%s, at=%s, load=%s]",
                id, loc, currentLoad
        );
    }
}