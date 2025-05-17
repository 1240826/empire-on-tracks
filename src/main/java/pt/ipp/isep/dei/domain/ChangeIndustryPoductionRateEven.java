package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.List;
import java.util.Objects;

/**
 * Evento que ajusta a taxa de produção de um template de indústria,
 * aplicando um incremento percentual sobre a produção atual.
 */
public class ChangeIndustryPoductionRateEven implements ScenarioEvent {
    private final int industryIndex;
    private final double percentageChange;

    /**
     * @param industryIndex    índice da indústria no IndustryRepository
     * @param percentageChange alteração percentual (por exemplo, 20 para +20%, -10 para -10%)
     */
    public ChangeIndustryPoductionRateEven(int industryIndex, double percentageChange) {
        this.industryIndex    = industryIndex;
        this.percentageChange = percentageChange;
    }
 @Override
 public void apply(Map map, List<Locomotive> locomotives) {
        List<Industry> inds = map.getIndustries();
        if (industryIndex < 0 || industryIndex >= inds.size()) {
            return;
        }
        Industry ind = Objects.requireNonNull(inds.get(industryIndex));

        double currentRate = ind.getProduction();
        double newRate     = currentRate + (percentageChange / 100.0) * currentRate;

     if (ind instanceof ProducingIndustry) {
         ProducingIndustry psi = (ProducingIndustry) ind;
         psi.setProduction(newRate);
     } else if (ind instanceof TransformingIndustry) {
         TransformingIndustry ti = (TransformingIndustry) ind;
         ti.setProduction(newRate);
     } else if (ind instanceof MixedIndustry) {
         MixedIndustry mi = (MixedIndustry) ind;
         mi.setProduction(newRate);
     }
        // A produção da instância no mapa foi modificada diretamente
    }


}