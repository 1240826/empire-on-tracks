package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Cargo;
import pt.ipp.isep.dei.domain.CargoType;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Period;
import pt.ipp.isep.dei.domain.Scenario;
import pt.ipp.isep.dei.repository.CargoRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.ScenarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Controller for the "Create Scenario" use case.
 * <p>
 * Manages the step-by-step creation of a new Scenario, including:
 * <ol>
 *   <li>Initializing with scenario period ticks.</li>
 *   <li>Selecting locomotive templates and setting their availability.</li>
 *   <li>Selecting cargo types and defining their behaviors.</li>
 *   <li>Configuring industry production frequency.</li>
 *   <li>Persisting the completed scenario.</li>
 * </ol>
 * <p>
 * Assumes the user is already authenticated and authorized to create scenarios.
 */
public class CreateScenarioController {
    private final ScenarioRepository   scenarioRepo = new ScenarioRepository();
    private final CargoRepository      cargoRepo    = new CargoRepository();
    private final LocomotiveRepository locoRepo     = LocomotiveRepository.getInstance();
    private final MapRepository        mapRepo      = new MapRepository();

    private Scenario scenario;

    /**
     * Begins a new scenario session.
     * <p>
     * Creates a Scenario with the specified trigger date and period.
     * Loads the current map for context.
     *
     * @param triggerDate date/time when the scenario will take effect
     * @param periodStart start tick of the scenario period
     * @param periodEnd   end tick of the scenario period
     * @throws NullPointerException     if triggerDate is null
     * @throws IllegalArgumentException if periodStart &gt; periodEnd
     */
    public void startCreateScenario(LocalDateTime triggerDate, int periodStart, int periodEnd) {
        Objects.requireNonNull(triggerDate, "triggerDate must not be null");
        Period period = new Period(periodStart, periodEnd);

        this.scenario = new Scenario(triggerDate);
        this.scenario.setPeriod(period);
        mapRepo.getCurrentMap();
    }

    /**
     * Retrieves all available locomotive templates from the repository.
     *
     * @return list of Locomotive prototypes for user selection
     */
    public List<Locomotive> getAllLocomotiveTemplates() {
        return locoRepo.getAllTemplates();
    }

    /**
     * Adds a cloned locomotive template to the scenario.
     *
     * @param templateIndex index of the template in the repository
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public void addLocomotiveByTemplate(int templateIndex) {
        Locomotive clone = locoRepo.getTemplate(templateIndex);
        if (clone == null) {
            throw new IllegalArgumentException("Invalid locomotive template index: " + templateIndex);
        }
        scenario.addLocomotive(clone);
    }

    /**
     * Sets the availability period for a specific locomotive in the scenario.
     *
     * @param locoIndex   index of the locomotive in the scenario list
     * @param periodStart start tick for availability
     * @param periodEnd   end tick for availability
     * @throws IllegalArgumentException if locoIndex is invalid or start &gt; end
     */
    public void setLocomotiveAvailability(int locoIndex, int periodStart, int periodEnd) {
        Period availability = new Period(periodStart, periodEnd);
        List<Locomotive> list = scenario.getLocomotives();
        if (locoIndex < 0 || locoIndex >= list.size()) {
            throw new IllegalArgumentException("Invalid locomotive index: " + locoIndex);
        }
        list.get(locoIndex).setAvailability(availability);
    }

    /**
     * Registers and adds a cargo type to the scenario.
     *
     * @param cargoType enum value representing the cargo
     * @throws NullPointerException if cargoType is null
     */
    public void addCargoType(CargoType cargoType) {
        Objects.requireNonNull(cargoType, "cargoType must not be null");
        Cargo c = new Cargo(cargoType,0);
        cargoRepo.addAvailableCargo(c);
        scenario.addCargo(c);
    }

    /**
     * Assigns a behavior to an existing cargo in the scenario.
     *
     * @param cargoType enum value of the cargo to modify
     * @param behavior  behavior string to assign (import/export/transform)
     */
    public void setCargoBehavior(CargoType cargoType, String behavior) {
        scenario.getCargos().stream()
                .filter(c -> c.getType() == cargoType)
                .findFirst()
                .ifPresent(c -> c.setBehavior(behavior));
    }

    /**
     * Sets the industry production frequency as a percentage of the scenario.
     *
     * @param percent production frequency (0-100)
     * @throws IllegalArgumentException if percent is outside the valid range
     */
    public void setIndustryProductionFrequency(int percent) {
        scenario.setIndustryProductionFrequency(percent);
    }

    /**
     * Persists the scenario to the repository and finalizes creation.
     *
     * @return true if the scenario was saved successfully
     */
    public boolean confirmScenarioCreation() {
        scenarioRepo.addScenario(scenario);
        return true;
    }
}
