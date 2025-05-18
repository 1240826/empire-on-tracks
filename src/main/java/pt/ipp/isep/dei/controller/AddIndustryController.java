package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller to add industries to a map.
 */

public class AddIndustryController {
    private final Repositories repositorios;
    private final Map mapa;
    private Location location;

    public AddIndustryController(Repositories repositorios, Map mapa) {
        this.repositorios = repositorios;
        this.mapa = mapa;
    }

    /**
     * Validates coordinates and prepares the Location.
     *
     * @param x X coordinate (column)
     * @param y Y coordinate (row)
     * @return true if coordinates are positive and the position is free
     */

    public boolean iniciarAdicaoIndustria(int x, int y) {
        ValueValidator validador = new ValueValidator();
        if (!validador.isPositive(x) || !validador.isPositive(y)) {
            System.out.println("Invalid coordinates.");
            return false;
        }
        if (!mapa.isEmpty(x, y)) {
            System.out.println("The position is already occupied.");
            return false;
        }
        this.location = new Location(x, y);
        return true;
    }

    /**
     * Creates and adds a new industry to the map, based on the selected template.
     *
     * @param indiceModelo index of the template in the repository.
     */

    public void adicionarIndustria(int indiceModelo) {
        Industry modelo =
                repositorios.getIndustryRepository()
                        .getIndustryTemplate(indiceModelo);

        if (modelo == null) {
            System.out.println("Invalid industry index.");
            return;
        }

        Industry novaIndustria;
        // clássico instanceof + cast
        if (modelo instanceof ProducingIndustry) {
            ProducingIndustry p = (ProducingIndustry) modelo;
            novaIndustria = new ProducingIndustry(
                    p.getIndustryName(),
                    p.getIndustryCost(),
                    p.getProduction(),
                    p.getDemand(),
                    location,
                    p.getCargoType()
            );
        }
        else if (modelo instanceof TransformingIndustry) {
            TransformingIndustry t = (TransformingIndustry) modelo;
            novaIndustria = new TransformingIndustry(
                    t.getIndustryName(),
                    t.getIndustryCost(),
                    t.getProduction(),
                    t.getDemand(),
                    location,
                    t.getInputCargoType(),
                    t.getOutputCargoType()
            );
        }
        else if (modelo instanceof MixedIndustry) {
            MixedIndustry m = (MixedIndustry) modelo;
            novaIndustria = new MixedIndustry(
                    m.getIndustryName(),
                    m.getIndustryCost(),
                    m.getProduction(),
                    m.getDemand(),
                    location,
                    m.getInputCargoType(),
                    m.getOutputCargoType()
            );
        }
        else {
            System.out.println("Industry type not supported.");
            return;
        }

        boolean adicionou = mapa.addIndustry(novaIndustria, location);
        if (adicionou) {
            System.out.println("Industry added successfully!");
        } else {
            System.out.println("Error adding the industry.");
        }
    }

    /**
     * @return list of all available industry templates
     */

    public List<Industry> obterModelosIndustria() {
        return repositorios
                .getIndustryRepository()
                .getListOfIndustry();
    }
}
