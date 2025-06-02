package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.CreateScenarioController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.MapRepository;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class CreateScenarioUI implements Runnable {
    private final CreateScenarioController controller = new CreateScenarioController();
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("--- Create Scenario ---");

        System.out.print("Enter a name for the scenario: ");
        String scenarioName = scanner.nextLine().trim();
        int startYear = 0, endYear = 0;
        boolean validInput = false;

        while (!validInput) {
            try {
                // Prompt for start year
                System.out.print("Enter the start year: ");
                startYear = Integer.parseInt(scanner.nextLine().trim());

                // Check if the start year is valid
                if (startYear < 1804) {
                    System.out.println("Start year must be later than 1804, the year the first locomotive appeared. Please try again.");
                    continue;
                }

                // Prompt for end year
                System.out.print("Enter the end year: ");
                endYear = Integer.parseInt(scanner.nextLine().trim());

                // Check if the end year is valid
                if (endYear < startYear) {
                    System.out.println("End year must be greater or equal to the start year. Please try again.");
                    continue;
                }

                // If input is valid, break the loop
                validInput = true;

                controller.startCreateScenario(scenarioName,startYear, endYear);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number.");
            }
        }
        showMainMenu();
        System.out.print("Confirm scenario creation? (Y/N): ");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("Y")) {
            controller.confirmScenarioCreation();
            System.out.println("Scenario saved successfully.");
        } else {
            System.out.println("Scenario discarded.");
        }
    }

    private void showMainMenu() {
        while (true) {
            System.out.println();
            System.out.println("Define Scenario restrictions:");
            System.out.println("----------------------------");
            System.out.println("1 - Available Locomotives");
            System.out.println("2 - Set Production Rate");
            System.out.println("3 - Define Mixed Industries Behavior");
            System.out.println("0 - Exit");
            System.out.println("----------------------------");
            System.out.print("Choose an option: ");
            String option = scanner.nextLine().trim();

            switch (option) {
                case "1":
                    showAvailableLocomotivesMenu();
                    break;
                case "2":
                    setProductionRate();
                    break;
                case "3":
                    mixedIndustryMenu();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Try again.\n");
            }
        }
    }

    private void showAvailableLocomotivesMenu() {
        while (true) {
            System.out.println("Do you want to add or remove a locomotive from the scenario?");
            System.out.println("1 - Add");
            System.out.println("2 - Remove");
            System.out.println("0 - Exit");
            System.out.print("Choose an option: ");
            String subOption = scanner.nextLine().trim();

            switch (subOption) {
                case "1":
                    addLocomotive();
                    break;
                case "2":
                    removeLocomotive();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option. Try again.\n");
            }
        }
    }

    private void addLocomotive() {
        List<Locomotive> templates = controller.getAllLocomotiveTemplates();
        System.out.println("------ Available Locomotives ------");
        for (int i = 0; i < templates.size(); i++) {
            System.out.printf("[%d] %s%n", i, templates.get(i));
        }

        System.out.print("Enter index of locomotive to add: ");
        int index = Integer.parseInt(scanner.nextLine().trim());

        try {
            controller.addLocomotiveByTemplate(index);
            System.out.println("Locomotive added to catalog.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void removeLocomotive() {
        List<Locomotive> catalogLocos = controller.getCatalogLocomotives();
        if (catalogLocos.isEmpty()) {
            System.out.println("No locomotives in catalog to remove.");
            return;
        }

        System.out.println("Locomotives in Catalog:");
        for (int i = 0; i < catalogLocos.size(); i++) {
            System.out.printf("[%d] %s%n", i, catalogLocos.get(i));
        }

        System.out.print("Enter index of locomotive to remove: ");
        int index = Integer.parseInt(scanner.nextLine().trim());

        try {
            controller.removeLocomotiveFromCatalog(index);
            System.out.println("Locomotive removed from catalog.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void mixedIndustryMenu() {
        IndustryRepository industryRepo = IndustryRepository.getInstance();
        List<Industry> industries = industryRepo.getListOfIndustry();

        List<MixedIndustry> mixedIndustries = industries.stream()
                .filter(industry -> industry instanceof MixedIndustry)
                .map(industry -> (MixedIndustry) industry)
                .collect(Collectors.toList());

        if (mixedIndustries.isEmpty()) {
            System.out.println("No Mixed Industries available.");
            return;
        }

        System.out.println("------ Mixed Industries ------");
        for (int i = 0; i < mixedIndustries.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, mixedIndustries.get(i).getIndustryName());
        }

        System.out.print("Choose a Mixed Industry to configure (0 to exit): ");
        int industryChoice = Integer.parseInt(scanner.nextLine().trim()) - 1;

        if (industryChoice == -1) {
            return;  // Exit the menu
        }

        if (industryChoice < 0 || industryChoice >= mixedIndustries.size()) {
            System.out.println("Invalid selection. Returning to main menu.");
            return;
        }

        MixedIndustry selectedIndustry = mixedIndustries.get(industryChoice);

        // Menu de opções: Import, Export, Transform
        System.out.println("Select an action for " + selectedIndustry.getIndustryName());
        System.out.println("1 - Import");
        System.out.println("2 - Export");
        System.out.println("3 - Transform");
        System.out.println("0 - Exit");

        System.out.print("Enter your choice: ");
        int actionChoice = Integer.parseInt(scanner.nextLine().trim());

        switch (actionChoice) {
            case 1: // Import
                handleImport(selectedIndustry);
                break;
            case 2: // Export
                handleExport(selectedIndustry);
                break;
            case 3: // Transform
                handleTransform(selectedIndustry);
                break;
            case 0: // Exit
                return;
            default:
                System.out.println("Invalid option. Returning to main menu.");
        }
    }

    private void handleImport(MixedIndustry industry) {
        // Para importar, demand é 0 e o cargo final é o output cargo
        industry.setDemand(0);
        industry.setProduction(0);  // Não há produção quando é importado
        industry.setOutputCargo(industry.getOutputCargoType());  // Cargo final
        System.out.println("Import setup complete for " + industry.getIndustryName());
    }

    private void handleExport(MixedIndustry industry) {
        // Para exportar, produção é 0 e a demanda é o cargo necessário
        industry.setProduction(0);  // Não há produção quando é exportado
        // Set demand to a required cargo amount (example based on your logic)
        industry.setDemand(industry.getProduction());
        System.out.println("Export setup complete for " + industry.getIndustryName());
    }

    private void handleTransform(MixedIndustry industry) {
        // Para transformar, manter a produção, demanda e o cargo final
        System.out.println("Transformation setup complete for " + industry.getIndustryName());
    }


    private void setProductionRate() {
        // Obtém a lista de todas as indústrias do repositório
        IndustryRepository industryRepo = IndustryRepository.getInstance();
        List<Industry> industries = industryRepo.getListOfIndustry(); // Lista das indústrias

        System.out.println("------ Available Industries ------");
        for (int i = 0; i < industries.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, industries.get(i).getIndustryName());
        }

        // Escolher uma indústria
        System.out.print("Choose an industry to set production rate: ");
        int industryIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;

        // Verificar se o índice é válido
        if (industryIndex < 0 || industryIndex >= industries.size()) {
            System.out.println("Invalid choice. Returning to main menu.");
            return;
        }

        Industry selectedIndustry = industries.get(industryIndex);

        // Exibir a produção atual da indústria
        System.out.println("Current Production Rate for " + selectedIndustry.getIndustryName() + ": " + selectedIndustry.getProduction() + " units");

        // Perguntar ao usuário o percentual de alteração na produção
        System.out.print("Enter percentage change for production rate (e.g., +20, -10): ");
        double percentageChange = Double.parseDouble(scanner.nextLine().trim());

        // Criar o evento para aplicar a alteração
        ChangeIndustryProductionRateEvent event = new ChangeIndustryProductionRateEvent(industryIndex, percentageChange);
        MapRepository mapRepo = new MapRepository();
        Map map = mapRepo.getCurrentMap();
        // Aplicar o evento no mapa
        event.apply(map, null);

        System.out.println("Production rate for " + selectedIndustry.getIndustryName() + " has been updated.");
    }

    /**
     * Entry‐point for Runnable dispatch.
     */
    @Override
    public void run() {
        start();
    }

    /**
     * Entry point for launching this console UI.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        new CreateScenarioUI().start();
    }
}
