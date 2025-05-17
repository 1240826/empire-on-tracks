package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;

import java.util.List;

/**
 * Contrato para qualquer evento de cenário que possa alterar
 * repositórios de indústrias ou locomotivas.
 */
public interface ScenarioEvent {
    void apply(Map map, List<Locomotive> locomotives);

    /**
     * Executa este evento, usando os repositórios para atualizar templates.
     *
     * @param industryRepo    repositório de indústrias
     * @param locomotiveRepo  repositório de locomotivas
     */

}
