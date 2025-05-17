package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.List;
import java.util.Objects;

/**
 * Evento que adiciona um novo template de Locomotive ao repositório geral.
 */
public class AddTemplateLocomotiveEvent implements ScenarioEvent {
    private final Locomotive template;

    /**
     * @param template protótipo de Locomotive a ser registrado
     */
    public AddTemplateLocomotiveEvent(Locomotive template) {
        this.template = Objects.requireNonNull(template);
    }


    @Override
    public void apply(Map map, List<Locomotive> locomotives) {

    }
}
