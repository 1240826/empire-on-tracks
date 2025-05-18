package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddIndustryController;
import pt.ipp.isep.dei.domain.Industry;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;
import java.util.Scanner;

/**
 * UI for adding industries to the map via the console.
 */
public class AddIndustryUI {
    private final AddIndustryController controller;
    private final Scanner scanner;

    public AddIndustryUI(Repositories repositorios, Map mapa) {
        this.controller = new AddIndustryController(repositorios, mapa);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Add Industry ===");

        // 1) Coordenadas
        System.out.print("Enter X coordinate: ");
        int x = scanner.nextInt();
        System.out.print("Enter Y coordinate: ");
        int y = scanner.nextInt();

        // 2) Valida e prepara localização
        if (!controller.iniciarAdicaoIndustria(x, y)) {
            System.out.println("Error initializing the process. Please try again.");
            return;
        }

        // 3) Exibe modelos de indústria
        List<Industry> modelos = controller.obterModelosIndustria();
        System.out.println("Available industry types:");
        for (int i = 0; i < modelos.size(); i++) {
            System.out.printf("%d – %s%n", i, modelos.get(i));
        }

        // 4) Escolha
        System.out.print("Choose the template index: ");
        int indice = scanner.nextInt();

        // 5) Adiciona ao mapa
        controller.adicionarIndustria(indice);
        System.out.println("Process completed.");
    }
}
