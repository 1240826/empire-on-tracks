package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.Objects;

/**
 * Evento que altera os tipos de carga de uma indústria já presente no mapa.
 */
public class ChangeIndustryCargoEvent implements ScenarioEvent {
    private final int industryIndex;
    private final CargoType newInput;
    private final CargoType newOutput;

    /**
     * @param industryIndex índice na lista map.getIndustries()
     * @param newInput      novo tipo de carga de entrada (null para não alterar)
     * @param newOutput     novo tipo de carga de saída (null para não alterar)
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
        List<Industry> inds = map.getIndustries();
        if (industryIndex < 0 || industryIndex >= inds.size()) {
            return;
        }
        Industry ind = Objects.requireNonNull(inds.get(industryIndex));

        if (ind instanceof TransformingIndustry) {
            TransformingIndustry ti = (TransformingIndustry) ind;
            if (newInput != null)  ti.setInputCargoType(newInput);
            if (newOutput != null) ti.setOutputCargoType(newOutput);
        }
        else if (ind instanceof MixedIndustry) {
            MixedIndustry mi = (MixedIndustry) ind;
            if (newInput != null)  mi.setInputCargoType(newInput);
            if (newOutput != null) mi.setOutputCargoType(newOutput);
        }
        else if (ind instanceof ProducingIndustry && newOutput != null) {
            ProducingIndustry pi = (ProducingIndustry) ind;
            pi.setCargoType(newOutput);
        }
        // nenhuma ação em indústrias que não suportam input/output
    }
}
