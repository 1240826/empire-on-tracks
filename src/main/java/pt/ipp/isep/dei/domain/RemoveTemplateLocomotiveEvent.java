package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.List;

/**
 * Evento que remove um template de Locomotive do repositório geral pelo índice.
 */
public class RemoveTemplateLocomotiveEvent implements ScenarioEvent {
    private final int templateIndex;

    /**
     * @param templateIndex índice do template em LocomotiveRepository.getAllTemplates()
     */
    public RemoveTemplateLocomotiveEvent(int templateIndex) {
        this.templateIndex = templateIndex;
    }



    @Override
    public void apply(Map map, List<Locomotive> locomotives) {

    }
}
