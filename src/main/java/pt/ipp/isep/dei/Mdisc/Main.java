package pt.ipp.isep.dei.Mdisc;

import pt.ipp.isep.dei.Mdisc.controller.AppController;

import java.util.Scanner;

/**
 * Entry point for the Railway Network application.
 * <p>
 * This class:
 * <ol>
 *   <li>Prompts the user for station and line CSV file paths.</li>
 *   <li>Asks the user to select a train type (electric, diesel, steam).</li>
 *   <li>Configures GraphStream properties for visualization.</li>
 *   <li>Initializes and starts the {@link AppController}.</li>
 * </ol>
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1) Read CSV file paths
        System.out.print("Path to stations CSV file: ");
        String stationsFile = sc.nextLine().trim();
        System.out.print("Path to lines CSV file:    ");
        String linesFile = sc.nextLine().trim();

        // 2) Select train type
        System.out.println("\nSelect train type:");
        System.out.println("1) Electric");
        System.out.println("2) Diesel");
        System.out.println("3) Steam");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine();
        boolean onlyElectrified = (choice == 1);

        // 3) Configure GraphStream for Swing
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty(
                "org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer"
        );

        // 4) Start the application controller
        AppController controller =
                new AppController(stationsFile, linesFile, onlyElectrified);
        controller.start();

        sc.close();
    }
}
