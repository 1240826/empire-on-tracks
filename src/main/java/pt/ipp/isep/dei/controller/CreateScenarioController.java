package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.util.List;
import java.util.Scanner;

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
    private final LocomotiveCatalog locoCatalog = new LocomotiveCatalog();
    private Scenario scenario;


    public void startCreateScenario(String name, int periodStart, int periodEnd) {
        Period period = new Period(periodStart, periodEnd);
        this.scenario = new Scenario(name, period);
        this.scenario.setPeriod(period);
        scenarioRepo.addScenario(this.scenario);
    }


    public List<Locomotive> getCatalogLocomotives() {
        return locoCatalog.getAllLocomotives();
    }
    public List<Locomotive> getAllLocomotiveTemplates(){
        return locoRepo.getAllTemplates();
    }

    public void addLocomotiveByTemplate(int templateIndex) {
        Locomotive template = locoRepo.getTemplate(templateIndex);
        if (template == null) {
            throw new IllegalArgumentException("Invalid template index.");
        }
        locoCatalog.addLocomotive(template); // Adiciona ao catálogo
    }

    public void removeLocomotiveFromCatalog(int index) {
        List<Locomotive> list = locoCatalog.getAllLocomotives();
        if (index < 0 || index >= list.size()) {
            throw new IllegalArgumentException("Invalid index.");
        }
        list.remove(index);
    }



    public boolean confirmScenarioCreation() {
        scenarioRepo.addScenario(scenario);
        return true;
    }
}
