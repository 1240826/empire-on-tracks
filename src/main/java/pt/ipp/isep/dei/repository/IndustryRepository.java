package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton que guarda modelos de Indústria e retorna cópias defensivas.
 */
public class IndustryRepository {
    private static IndustryRepository instance;
    private final List<Industry> industriesModel;

    private IndustryRepository() {
        industriesModel = new ArrayList<>();

        // ProducingIndustry
        industriesModel.add(new ProducingIndustry(
                "Solar Farm", 700, 15, 20, CargoType.COAL
        ));
        industriesModel.add(new ProducingIndustry(
                "Coal Mine", 1200, 100, 150, CargoType.COAL
        ));

        // TransformingIndustry
        industriesModel.add(new TransformingIndustry(
                "Steel Factory", 1500, 90, 10,
                CargoType.COAL, CargoType.STEEL
        ));
        industriesModel.add(new TransformingIndustry(
                "Food Processor", 1100, 60, 70,
                CargoType.PLASTIC, CargoType.FOOD
        ));

        // MixedIndustry
        industriesModel.add(new MixedIndustry(
                "Tech Plant", 2000, 50, 90,
                CargoType.PLASTIC, CargoType.TECHNOLOGY
        ));
        industriesModel.add(new MixedIndustry(
                "Oil Refinery", 1800, 120, 70,
                CargoType.OIL, CargoType.PLASTIC
        ));
    }

    public static IndustryRepository getInstance() {
        if (instance == null) {
            instance = new IndustryRepository();
        }
        return instance;
    }

    /**
     * Retorna uma nova lista com todos os modelos disponíveis.
     */
    public List<Industry> getListOfIndustry() {
        return new ArrayList<>(industriesModel);
    }

    /**
     * Retorna uma cópia defensiva do modelo no índice fornecido.
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
        }
        else if (model instanceof TransformingIndustry) {
            TransformingIndustry t = (TransformingIndustry) model;
            return new TransformingIndustry(
                    t.getIndustryName(),
                    t.getIndustryCost(),
                    t.getProduction(),
                    t.getDemand(),
                    t.getInputCargoType(),
                    t.getOutputCargoType()
            );
        }
        else if (model instanceof MixedIndustry) {
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

        // Se for algum outro tipo de Industry (não esperado), devolve o próprio
        return model;
    }
}
