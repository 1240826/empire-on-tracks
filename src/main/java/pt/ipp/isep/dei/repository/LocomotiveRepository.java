package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.domain.Locomotive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repositório singleton para os templates de Locomotive.
 * Guarda uma lista de protótipos que podem ser clonados ao instanciar novos.
 */
public class LocomotiveRepository {
    private static LocomotiveRepository instance;
    private final List<Locomotive> templates = new ArrayList<>();

    private LocomotiveRepository() {
        // Templates padrão
        // Steam loco
        templates.add(new Locomotive(
                "S100", "Steam", 75000.0, false,
                2000, 0.5, 80.0, 1850,
                10000.0, 5000.0
        ));
        // Diesel loco
        templates.add(new Locomotive(
                "D200", "Diesel", 120000.0, false,
                3000, 0.8, 120.0, 1920,
                15000.0, 7000.0
        ));
        // Electric loco
        templates.add(new Locomotive(
                "E300", "Electric", 200000.0, true,
                4000, 1.0, 160.0, 1950,
                8000.0, 6000.0
        ));
    }

    /** Obtém a instância singleton. */
    public static synchronized LocomotiveRepository getInstance() {
        if (instance == null) {
            instance = new LocomotiveRepository();
        }
        return instance;
    }

    /** Retorna todos os templates cadastrados (cópia da lista). */
    public List<Locomotive> getAllTemplates() {
        return new ArrayList<>(templates);
    }

    /**
     * Retorna um novo objeto Locomotive baseado no template indicado.
     * @param index índice do template
     * @return clone do template, ou null se índice inválido
     */
    public Locomotive getTemplate(int index) {
        if (index < 0 || index >= templates.size()) {
            return null;
        }
        Locomotive tpl = templates.get(index);
        // supomos que Locomotive tenha um construtor que receba todos os campos
        return new Locomotive(
                tpl.getId(),
                tpl.getType(),
                tpl.getPrice(),
                tpl.requiresElectrification(),
                tpl.getPower(),
                tpl.getAcceleration(),
                tpl.getTopSpeed(),
                tpl.getStartYear(),
                tpl.getFuelCostPerYear(),
                tpl.getMaintenancePerYear()
        );
    }

    /** Adiciona um novo template de locomotiva. */
    public void addTemplate(Locomotive loco) {
        templates.add(Objects.requireNonNull(loco));
    }

    /**
     * Substitui o template no índice indicado pelo novo objeto.
     * @param index índice do template a atualizar
     * @param loco  nova definição
     */
    public void updateTemplate(int index, Locomotive loco) {
        if (index >= 0 && index < templates.size()) {
            templates.set(index, Objects.requireNonNull(loco));
        }
    }

    /** Remove o template no índice indicado. */
    public void removeTemplate(int index) {
        if (index >= 0 && index < templates.size()) {
            templates.remove(index);
        }
    }
}
