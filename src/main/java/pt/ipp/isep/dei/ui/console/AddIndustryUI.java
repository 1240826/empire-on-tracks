package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddIndustryController;
import pt.ipp.isep.dei.domain.Industry;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;
import java.util.Scanner;

/**
 * UI para adicionar indústrias ao mapa pelo console.
 */
public class AddIndustryUI {
    private final AddIndustryController controller;
    private final Scanner scanner;

    public AddIndustryUI(Repositories repositorios, Map mapa) {
        this.controller = new AddIndustryController(repositorios, mapa);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Adicionar Indústria ===");

        // 1) Coordenadas
        System.out.print("Introduza a coordenada X: ");
        int x = scanner.nextInt();
        System.out.print("Introduza a coordenada Y: ");
        int y = scanner.nextInt();

        // 2) Valida e prepara localização
        if (!controller.iniciarAdicaoIndustria(x, y)) {
            System.out.println("Erro ao iniciar o processo. Tente novamente.");
            return;
        }

        // 3) Exibe modelos de indústria
        List<Industry> modelos = controller.obterModelosIndustria();
        System.out.println("Tipos de indústria disponíveis:");
        for (int i = 0; i < modelos.size(); i++) {
            System.out.printf("%d – %s%n", i, modelos.get(i));
        }

        // 4) Escolha
        System.out.print("Escolha o índice do modelo: ");
        int indice = scanner.nextInt();

        // 5) Adiciona ao mapa
        controller.adicionarIndustria(indice);
        System.out.println("Processo concluído.");
    }
}
