package pt.ipp.isep.dei.domain;

import java.util.List;

/**
 * Evento que altera os tipos de carga de uma indústria no mapa.
 */
public class ChangeIndustryCargoEvent implements ScenarioEvent {
    private final int industryIndex;
    private final CargoType newInput;
    private final CargoType newOutput;

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
