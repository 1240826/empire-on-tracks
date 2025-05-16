package pt.ipp.isep.dei.ui.console;

import pt.ipp.isep.dei.controller.AddCityController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.HouseBlock;
import pt.ipp.isep.dei.domain.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddCityUI {
    private final AddCityController controller = new AddCityController();

    public void start() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Add New City ===");

        // 1) Inicializa controller e obtém o mapa atual
        controller.startAddCity();

        // 2) Coleta dados básicos da cidade
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Position X: ");
        int x = sc.nextInt();
        System.out.print("Position Y: ");
        int y = sc.nextInt();
        System.out.print("Number of House Blocks: ");
        int hb = sc.nextInt();
        sc.nextLine(); // consome newline

        City city;
        try {
            // 3) Cria e valida city, incluindo duplicação de nome
            city = controller.submitCityDetails(name, x, y, hb);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            sc.close();
            return;
        }

        // 4) Escolhe método de atribuição de blocos
        System.out.print("Assignment method (automatic/manual): ");
        String method = sc.nextLine().trim().toLowerCase();

        List<Location> positions = new ArrayList<>();
        if ("manual".equals(method)) {
            for (int i = 1; i <= hb; i++) {
                System.out.print("Block " + i + " X: ");
                int bx = sc.nextInt();
                System.out.print("Block " + i + " Y: ");
                int by = sc.nextInt();
                positions.add(new Location(bx, by));
            }
            sc.nextLine(); // consome newline
        }

        try {
            controller.selectedAssignmentMethod(method, positions);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            sc.close();
            return;
        }

        // 6) Exibe revisão e pede confirmação
        System.out.println("\nReview City:");
        System.out.println("Name:   " + city.getName());
        System.out.println("Center: (" + city.getCenter().getX() + ", " + city.getCenter().getY() + ")");
        System.out.println("House Blocks:");
        for (HouseBlock hbk : city.getHouseBlocks()) {
            System.out.println(" - (" + hbk.getPosition().getX() + ", " + hbk.getPosition().getY() + ")");
        }

        System.out.print("\nConfirm creation? (yes/no): ");
        String confirm = sc.nextLine().trim().toLowerCase();
        if ("yes".equals(confirm)) {
            controller.confirmCityCreation();
            System.out.println("City created successfully!");
        } else {
            System.out.println("Operation cancelled.");
        }

        sc.close();
    }
}
