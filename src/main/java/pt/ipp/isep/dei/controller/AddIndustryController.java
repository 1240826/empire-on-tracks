package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controlador para adicionar indústrias ao mapa.
 */
public class AddIndustryController {
    private final Repositories repositorios;
    private final Map mapa;
    private Location location;

    public AddIndustryController(Repositories repositorios, Map mapa) {
        this.repositorios = repositorios;
        this.mapa = mapa;
    }

    /**
     * Valida coordenadas e prepara a Location.
     *
     * @param x coordenada X (coluna)
     * @param y coordenada Y (linha)
     * @return true se as coordenadas forem positivas e a posição estiver livre
     */
    public boolean iniciarAdicaoIndustria(int x, int y) {
        ValueValidator validador = new ValueValidator();
        if (!validador.isPositive(x) || !validador.isPositive(y)) {
            System.out.println("Coordenadas inválidas.");
            return false;
        }
        if (!mapa.isEmpty(x, y)) {
            System.out.println("A posição está ocupada.");
            return false;
        }
        this.location = new Location(x, y);
        return true;
    }

    /**
     * Cria e adiciona ao mapa uma nova indústria, com base no modelo escolhido.
     *
     * @param indiceModelo índice do modelo no repositório
     */
    public void adicionarIndustria(int indiceModelo) {
        Industry modelo =
                repositorios.getIndustryRepository()
                        .getIndustryTemplate(indiceModelo);

        if (modelo == null) {
            System.out.println("Índice de indústria inválido.");
            return;
        }

        Industry novaIndustria;
        // clássico instanceof + cast
        if (modelo instanceof ProducingIndustry) {
            ProducingIndustry p = (ProducingIndustry) modelo;
            novaIndustria = new ProducingIndustry(
                    p.getIndustryName(),
                    p.getIndustryCost(),
                    p.getProduction(),
                    p.getDemand(),
                    location,
                    p.getCargoType()
            );
        }
        else if (modelo instanceof TransformingIndustry) {
            TransformingIndustry t = (TransformingIndustry) modelo;
            novaIndustria = new TransformingIndustry(
                    t.getIndustryName(),
                    t.getIndustryCost(),
                    t.getProduction(),
                    t.getDemand(),
                    location,
                    t.getInputCargoType(),
                    t.getOutputCargoType()
            );
        }
        else if (modelo instanceof MixedIndustry) {
            MixedIndustry m = (MixedIndustry) modelo;
            novaIndustria = new MixedIndustry(
                    m.getIndustryName(),
                    m.getIndustryCost(),
                    m.getProduction(),
                    m.getDemand(),
                    location,
                    m.getInputCargoType(),
                    m.getOutputCargoType()
            );
        }
        else {
            System.out.println("Tipo de indústria não suportado.");
            return;
        }

        boolean adicionou = mapa.addIndustry(novaIndustria, location);
        if (adicionou) {
            System.out.println("Indústria adicionada com sucesso!");
        } else {
            System.out.println("Erro ao adicionar a indústria.");
        }
    }

    /**
     * @return lista de todos os modelos de indústria disponíveis
     */
    public List<Industry> obterModelosIndustria() {
        return repositorios
                .getIndustryRepository()
                .getListOfIndustry();
    }
}
