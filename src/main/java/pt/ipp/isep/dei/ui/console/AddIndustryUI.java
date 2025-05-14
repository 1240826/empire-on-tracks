package pt.ipp.isep.dei.ui.console;


import pt.ipp.isep.dei.controller.AddIndustryController;
import pt.ipp.isep.dei.domain.Industry;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;


import java.util.List;
import java.util.Scanner;

public class AddIndustryUI {
    private final AddIndustryController controller;
    private final Scanner scanner;

    public AddIndustryUI(Repositories repositories, Map map) {
        this.controller = new AddIndustryController(repositories, map);
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("=== Adicionar Indústria ===");

        // Pedir coordenadas ao utilizador
        System.out.print("Introduza a coordenada X: ");
        int x = scanner.nextInt();

        System.out.print("Introduza a coordenada Y: ");
        int y = scanner.nextInt();

        // Iniciar processo de adição
        boolean validStart = controller.startAddIndustryController(x, y);
        if (!validStart) {
            System.out.println("Erro ao iniciar o processo. Tente novamente.");
            return;
        }

        // Mostrar lista de indústrias disponíveis
        List<Industry> industryList = controller.getAvailableIndustries();
        System.out.println("Tipos de indústria disponíveis:");
        for (int i = 0; i < industryList.size(); i++) {
            System.out.println(i + " - " + industryList.get(i).toString());
        }

        // Perguntar opção
        System.out.print("Escolha o índice da indústria: ");
        int index = scanner.nextInt();

        // Adicionar indústria
        controller.addIndustry(index);
        System.out.println("Processo concluído.");
    }
}

