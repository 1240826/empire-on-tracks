package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;

import java.util.List;

public class AddIndustryController {
    private Repositories repositories;
    private Map map;
    private Location location;

    public AddIndustryController(Repositories repositories, Map map) {
        this.repositories = repositories;
        this.map = map;
    }

    /**
     * Inicia o processo de adição de indústria.
     * Solicita as coordenadas ao usuário e valida.
     */
    public boolean startAddIndustryController(int x, int y) {
        // Valida as coordenadas (usando ValueValidator)
        ValueValidator validator = new ValueValidator();
        if (!validator.isPositive(x) || !validator.isPositive(y)) {
            System.out.println("Coordenadas inválidas.");
            return false;
        }

        // Verifica se a posição no mapa está livre
        if (!mapPositionIsEmpty(x, y)) {
            System.out.println("A posição está ocupada.");
            return false;
        }

        // Cria a localização
        location = new Location(x, y);
        return true;
    }

    /**
     * Verifica se o local no mapa está vazio.
     */
    private boolean mapPositionIsEmpty(int x, int y) {
        return map.getMap()[x][y] == null;
    }

    /**
     * Adiciona a indústria ao mapa.
     * Primeiro, escolhe a indústria e cria a instância apropriada.
     */
    public void addIndustry(int industryIndex) {
        // Recupera o template de indústrias do repositório
        IndustryRepository industryRepo = repositories.getIndustryRepository();
        Industry industryTemplate = industryRepo.getIndustryTemplate(industryIndex);

        // Cria uma nova indústria baseada no template
        Industry newIndustry = null;

        if (industryTemplate instanceof ProducingIndustry) {
            newIndustry = new ProducingIndustry(industryTemplate.getindustryname(),
                    industryTemplate.getIndustryCost(),
                    industryTemplate.getProduction(),
                    industryTemplate.getDemand());
        } else if (industryTemplate instanceof TransformingIndustry) {
            newIndustry = new TransformingIndustry(industryTemplate.getindustryname(),
                    industryTemplate.getIndustryCost(),
                    industryTemplate.getProduction(),
                    industryTemplate.getDemand());
        } else if (industryTemplate instanceof MixedIndustry) {
            newIndustry = new MixedIndustry(industryTemplate.getindustryname(),
                    industryTemplate.getIndustryCost(),
                    industryTemplate.getProduction(),
                    industryTemplate.getDemand());
        }

        // Define a localização da nova indústria
        if (newIndustry != null) {
            newIndustry.setLocation(location);

            // Adiciona a indústria ao mapa
            map.getMap()[location.getX()][location.getY()] = newIndustry;
            System.out.println("Indústria adicionada com sucesso!");
        } else {
            System.out.println("Erro ao criar a indústria.");
        }
    }
    public List<Industry> getAvailableIndustries() {
        return repositories.getIndustryRepository().getListOfIndustry();
    }

}

