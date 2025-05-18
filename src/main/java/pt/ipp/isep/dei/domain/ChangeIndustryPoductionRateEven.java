package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;
import pt.ipp.isep.dei.repository.ScenarioRepository;

import java.util.List;
import java.util.Objects;

/**
 * Event that adjusts the production rate of an industry template,
 * applying a percentage increment or decrement to the current production rate.
 */
public class ChangeIndustryPoductionRateEven implements ScenarioEvent {
    private final int industryIndex;
    private final double percentageChange;

    /**
     * Constructs a ChangeIndustryPoductionRateEven event.
     *
     * @param industryIndex    the index of the industry in the IndustryRepository
     * @param percentageChange the percentage change to apply (e.g., 20 for +20%, -10 for -10%)
     */
    public ChangeIndustryPoductionRateEven(int industryIndex, double percentageChange) {
        this.industryIndex    = industryIndex;
        this.percentageChange = percentageChange;
    }

    /**
     * Applies the event to the specified map and list of locomotives.
     * Adjusts the production rate of the target industry (if found) according to the percentage change.
     *
     * @param map         the map containing industries to be modified
     * @param locomotives the list of locomotives (unused in this event)
     */
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
        // The production of the instance in the map is modified directly.
    }
}
