package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IndustryRepository {
    private static IndustryRepository instance;
    private List<Industry> industriesTemplate;

    public IndustryRepository(){
        industriesTemplate = new ArrayList<>();

        industriesTemplate.add(new ProducingIndustry("Solar Farm", 700, 15, 20));
        industriesTemplate.add(new ProducingIndustry("Coal Mine", 1200, 100, 150));

        industriesTemplate.add(new TransformingIndustry("Steel Factory", 1500, 90, 10));
        industriesTemplate.add(new TransformingIndustry("Food Processor", 1100, 60, 70));

        industriesTemplate.add(new MixedIndustry("Tech Plant", 2000, 50, 90));
        industriesTemplate.add(new MixedIndustry("Oil Refinery", 1800, 120, 70));
    }
    public List<Industry> getListOfIndustry() {
        return industriesTemplate;
    }
    public Industry getIndustryTemplate(int index) {
        if (index >= 0 && index < industriesTemplate.size()) {
            Industry template = industriesTemplate.get(index);
            if (template instanceof ProducingIndustry) {
                return new ProducingIndustry(template.getindustryname(), template.getIndustryCost(), template.getProduction(), template.getDemand());
            } else if (template instanceof TransformingIndustry) {
                return new TransformingIndustry(template.getindustryname(), template.getIndustryCost(), template.getProduction(), template.getDemand());
            } else if (template instanceof MixedIndustry) {
                return new MixedIndustry(template.getindustryname(), template.getIndustryCost(), template.getProduction(), template.getDemand());
            }
        }
        return null;
    }

    public static IndustryRepository getInstance() {
        if (instance == null) {
            instance = new IndustryRepository();
        }
        return instance;
    }
}
