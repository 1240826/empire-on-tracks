package pt.ipp.isep.dei.mapper;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts {@code List<Industry>} to {@code List<IndustryDTO>}
 * including cargo type and quantity.
 */
public final class IndustryMapper {

    private IndustryMapper() { }

    public static List<IndustryDTO> toDTO(List<Industry> models) {
        List<IndustryDTO> list = new ArrayList<>(models.size());

        for (int i = 0; i < models.size(); i++) {
            Industry m = models.get(i);

            String consumesType;
            double consumesQty;
            String producesType;
            double producesQty;

            if (m instanceof ProducingIndustry p) {
                consumesType = "–";
                consumesQty  = 0;
                producesType = p.getCargoType().name();
                producesQty  = p.getProduction();
            } else if (m instanceof TransformingIndustry t) {
                consumesType = t.getInputCargoType().name();
                consumesQty  = t.getDemand();
                producesType = t.getOutputCargoType().name();
                producesQty  = t.getProduction();
            } else if (m instanceof MixedIndustry x) {
                consumesType = x.getInputCargoType().name();
                consumesQty  = x.getDemand();
                producesType = x.getOutputCargoType().name();
                producesQty  = x.getProduction();
            } else {
                consumesType = "–";
                consumesQty  = 0;
                producesType = "UNKNOWN";
                producesQty  = 0;
            }

            list.add(new IndustryDTO(
                    i,
                    m.getIndustryName(),
                    consumesType,
                    consumesQty,
                    producesType,
                    producesQty
            ));
        }
        return list;
    }
}
