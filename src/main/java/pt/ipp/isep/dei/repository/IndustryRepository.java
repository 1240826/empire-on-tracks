package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton repository that holds industry model templates and provides
 * defensive copies to callers.
 * <p>
 * On initialization, this repository creates a set of default industry models:
 * producing, transforming, and mixed industries with various cargo types.
 * Clients can retrieve the full list of templates or a single template copy
 * by index.
 * </p>
 */
public class IndustryRepository {

    /** Singleton instance of this repository. */
    private static IndustryRepository instance;

    /** Internal list of industry templates. */
    private final List<Industry> industriesModel;

    /**
     * Private constructor that initializes the model list
     * with predefined industry templates.
     */
    private IndustryRepository() {
        industriesModel = new ArrayList<>();

        // ProducingIndustry templates
        industriesModel.add(new ProducingIndustry(
                "Solar Farm", 700, 15, 20, CargoType.COAL
        ));
        industriesModel.add(new ProducingIndustry(
                "Coal Mine", 1200, 100, 150, CargoType.COAL
        ));

        // TransformingIndustry templates
        industriesModel.add(new TransformingIndustry(
                "Steel Factory", 1500, 90, 10,
                CargoType.COAL, CargoType.STEEL
        ));
        industriesModel.add(new TransformingIndustry(
                "Food Processor", 1100, 60, 70,
                CargoType.PLASTIC, CargoType.FOOD
        ));

        // MixedIndustry templates
        industriesModel.add(new MixedIndustry(
                "Tech Plant", 2000, 50, 90,
                CargoType.PLASTIC, CargoType.TECHNOLOGY
        ));
        industriesModel.add(new MixedIndustry(
                "Oil Refinery", 1800, 120, 70,
                CargoType.OIL, CargoType.PLASTIC
        ));
    }

    /**
     * Returns the singleton instance of the repository,
     * creating it if necessary.
     *
     * @return the {@link IndustryRepository} singleton
     */
    public static IndustryRepository getInstance() {
        if (instance == null) {
            instance = new IndustryRepository();
        }
        return instance;
    }

    /**
     * Returns a new {@code List} containing all available industry templates.
     * <p>
     * The returned list is a defensive copy of the internal model list.
     * </p>
     *
     * @return a {@code List<Industry>} of template instances
     */
    public List<Industry> getListOfIndustry() {
        return new ArrayList<>(industriesModel);
    }

    /**
     * Returns a defensive copy of the industry template at the given index.
     * <p>
     * Supports ProducingIndustry, TransformingIndustry, and MixedIndustry.
     * If the index is out of bounds, returns {@code null}.
     * </p>
     *
     * @param index the zero-based index of the desired template
     * @return a new {@link Industry} instance matching the template, or {@code null} if index invalid
     */
    public Industry getIndustryTemplate(int index) {
        if (index < 0 || index >= industriesModel.size()) {
            return null;
        }
        Industry model = industriesModel.get(index);

        if (model instanceof ProducingIndustry) {
            ProducingIndustry p = (ProducingIndustry) model;
            return new ProducingIndustry(
                    p.getIndustryName(),
                    p.getIndustryCost(),
                    p.getProduction(),
                    p.getDemand(),
                    p.getCargoType()
            );
        } else if (model instanceof TransformingIndustry) {
            TransformingIndustry t = (TransformingIndustry) model;
            return new TransformingIndustry(
                    t.getIndustryName(),
                    t.getIndustryCost(),
                    t.getProduction(),
                    t.getDemand(),
                    t.getInputCargoType(),
                    t.getOutputCargoType()
            );
        } else if (model instanceof MixedIndustry) {
            MixedIndustry m = (MixedIndustry) model;
            return new MixedIndustry(
                    m.getIndustryName(),
                    m.getIndustryCost(),
                    m.getProduction(),
                    m.getDemand(),
                    m.getInputCargoType(),
                    m.getOutputCargoType()
            );
        }

        // For any other Industry implementation, return the original instance
        return model;
    }
}
