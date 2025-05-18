package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Event that changes the cargo types of an industry on the map.
 */
public class ChangeIndustryCargoEvent implements ScenarioEvent {
    private final int industryIndex;
    private final CargoType newInput;
    private final CargoType newOutput;

    /**
     * Constructs a ChangeIndustryCargoEvent with the specified industry index,
     * new input cargo type, and new output cargo type.
     *
     * @param industryIndex the index of the industry to update in the map's industry list
     * @param newInput      the new input cargo type (nullable)
     * @param newOutput     the new output cargo type (nullable)
     */
    public ChangeIndustryCargoEvent(int industryIndex,
                                    CargoType newInput,
                                    CargoType newOutput) {
        this.industryIndex = industryIndex;
        this.newInput      = newInput;
        this.newOutput     = newOutput;
    }

    @Override
    public void apply(Map map, List<Locomotive> locomotives) {
        Industry ind;
        try {
            ind = map.getIndustries().get(industryIndex);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        if (ind instanceof TransformingIndustry) {
            TransformingIndustry ti = (TransformingIndustry) ind;
            if (newInput  != null) ti.setInputCargo(newInput);
            if (newOutput != null) ti.setOutputCargo(newOutput);
        }
        else if (ind instanceof MixedIndustry) {
            MixedIndustry mi = (MixedIndustry) ind;
            if (newInput  != null) mi.setInputCargo(newInput);
            if (newOutput != null) mi.setOutputCargo(newOutput);
        }
        else if (ind instanceof ProducingIndustry) {
            ProducingIndustry pi = (ProducingIndustry) ind;
            if (newOutput != null) pi.setCargoType(newOutput);
        }
        // outras instâncias de Industry não sofrem alteração de carga
    }
}
