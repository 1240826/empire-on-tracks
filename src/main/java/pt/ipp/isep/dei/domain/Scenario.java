package pt.ipp.isep.dei.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a simulation scenario, including a trigger date, time period,
 * and configured resources such as locomotives, cargos, and industry production settings.
 */
public class Scenario {
    // Configured resources for this scenario
    private final List<Locomotive> locomotives = new ArrayList<>();
    private final List<Cargo> cargos = new ArrayList<>();
    private final String name;

    // Time period over which the scenario runs
    private Period period;

    // Industry production frequency (percentage)
    private int industryProductionFrequency;


    public Scenario(String name, Period period) {
        Objects.requireNonNull(period, "period must not be null");
        this.name = name;
    }

    public String getName() {
        return name;
    }


    /**
     * Sets the time period for the scenario execution.
     *
     * @param period the period over which the scenario runs
     * @throws NullPointerException if period is null
     */
    public void setPeriod(Period period) {
        Objects.requireNonNull(period, "period must not be null");

        Integer earliestLocoYear = getEarliestLocomotiveStartYear();
        if (earliestLocoYear != null && period.getStartDate() < earliestLocoYear) {
            throw new IllegalArgumentException("Scenario start year must be >= " + earliestLocoYear);
        }

        this.period = period;
    }

    private Integer getEarliestLocomotiveStartYear() {
        return locomotives.stream()
                .map(Locomotive::getStartYear)
                .min(Integer::compareTo)
                .orElse(null);
    }

    /**
     * Returns the time period of the scenario.
     *
     * @return the simulation period
     */
    public Period getPeriod() {
        return period;
    }

    /**
     * Adds a locomotive to this scenario's configuration.
     *
     * @param loco the locomotive to add
     * @throws NullPointerException if loco is null
     */
    public void addLocomotive(Locomotive loco) {
        locomotives.add(Objects.requireNonNull(loco, "loco must not be null"));
    }

    /**
     * Returns an immutable list of configured locomotives.
     *
     * @return list of locomotives
     */
    public List<Locomotive> getLocomotives() {
        return new ArrayList<>(locomotives);
    }

    /**
     * Adds a cargo type to this scenario's configuration.
     *
     * @param cargo the cargo to add
     * @throws NullPointerException if cargo is null
     */
    public void addCargo(Cargo cargo) {
        cargos.add(Objects.requireNonNull(cargo, "cargo must not be null"));
    }

    /**
     * Returns an immutable list of configured cargos.
     *
     * @return list of cargos
     */
    public List<Cargo> getCargos() {
        return new ArrayList<>(cargos);
    }

    /**
     * Sets the industry production frequency, as a percentage, for this scenario.
     *
     * @param percent production frequency between 0 and 100 inclusive
     * @throws IllegalArgumentException if percent is outside the range [0, 100]
     */
    public void setIndustryProductionFrequency(int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException("percent must be between 0 and 100");
        }
        this.industryProductionFrequency = percent;
    }

    /**
     * Returns the configured industry production frequency.
     *
     * @return production frequency percentage
     */
    public int getIndustryProductionFrequency() {
        return industryProductionFrequency;
    }

    /**
     * Applies all resources of this scenario to the provided map.
     * <p>
     * Example: places each locomotive onto the map to start its route.
     * Additional resource application (cargo distribution, industry setup) can be implemented here.
     *
     * @param map         the Map context to which resources are applied
     * @param locomotives list of locomotives to place on the map
     * @return
     * @throws NullPointerException if map or locomotives is null
     */
    public List<String> applyAll(Map map, List<Locomotive> locomotives) {
        Objects.requireNonNull(map, "map must not be null");
        Objects.requireNonNull(locomotives, "locomotives must not be null");
        for (Locomotive loco : locomotives) {
            map.addLocomotive(loco);
        }
        return null;
    }
}

