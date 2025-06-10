package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddCityController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.Map;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class AddCityUI implements Runnable {

    private AddCityController controller;

    public void start(Map map) {
        Scanner scanner = new Scanner(System.in);
        controller = new AddCityController(map);

        while (true) {
            System.out.println("\n=== City Management Menu ===");
            System.out.println("1. Add new city");
            System.out.println("2. Remove city");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    addCity(scanner);
                    break;
                case 2:
                    removeCity(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void addCity(Scanner scanner) {
        System.out.print("Enter city name: ");
        String name = scanner.nextLine();

        // Pede as coordenadas da cidade
        System.out.print("Enter city X position: ");
        int posX = scanner.nextInt();

        System.out.print("Enter city Y position: ");
        int posY = scanner.nextInt();

        // Pede a quantidade de blocos de casa
        System.out.print("Enter number of house blocks: ");
        int houseBlockCount = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Choose house block assignment method:");
        System.out.println("1. Manual");
        System.out.println("2. Automatic");
        System.out.print("Your choice: ");
        int methodOption = scanner.nextInt();
        scanner.nextLine();
        String method = (methodOption == 1) ? "manual" : "automatic";

        // Caso o método seja manual, pedimos as coordenadas dos blocos
        if (method.equals("manual")) {
            List<Integer> posXArray = new ArrayList<>();
            List<Integer> posYArray = new ArrayList<>();

            for (int i = 0; i < houseBlockCount; i++) {
                System.out.println("Enter coordinates for House Block #" + (i + 1));
                System.out.print("  X: ");
                posXArray.add(scanner.nextInt());
                System.out.print("  Y: ");
                posYArray.add(scanner.nextInt());
            }

            // Criar a cidade com o método manual
            City city = controller.createCity(name, posX, posY, houseBlockCount, method, posXArray, posYArray);
            displaySuccess(city);
        } else {
            // Criar a cidade com o método automático
            City city = controller.createCity(name, posX, posY, houseBlockCount, method, null, null);
            displaySuccess(city);
        }
    }

    private void removeCity(Scanner scanner) {
        System.out.print("Enter the name of the city to remove: ");
        String name = scanner.nextLine();

        boolean removed = controller.removeCity(name);
        if (removed) {
            System.out.println("✅ City successfully removed.");
        } else {
            System.out.println("❌ City not found.");
        }
    }

    private void displaySuccess(City city) {
        if (city != null) {
            System.out.println("\n✅ City created successfully: " + city.getName());
        } else {
            System.out.println("\n❌ Failed to create city. Name might be invalid or already exists.");
        }
    }

    @Override
    public void run() {
        Map map = new Map();
        start(map);
    }
}
