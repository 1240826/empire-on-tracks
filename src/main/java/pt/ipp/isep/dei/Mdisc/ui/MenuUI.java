package pt.ipp.isep.dei.Mdisc.ui;

import java.util.Scanner;

/**
 * Console-based menu for the railway network application.
 * <p>
 * Displays options to the user and reads their selection.
 */
public class MenuUI {
    private final boolean onlyElectrified;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructs a new MenuUI.
     *
     * @param onlyElectrified if true, show that only electrified lines are considered
     */
    public MenuUI(boolean onlyElectrified) {
        this.onlyElectrified = onlyElectrified;
    }

    /**
     * Shows the main menu and returns the user's choice.
     *
     * @return the selected option number
     */
    public int showMenu() {
        System.out.println();
        System.out.println("========= MAIN MENU =========");
        if (onlyElectrified) {
            System.out.println("| Electrified only: YES           |");
        } else {
            System.out.println("| Electrified only: NO            |");
        }
        System.out.println("-----------------------------");
        System.out.println(" 1) Export adjacency matrix to file");
        System.out.println(" 2) Check reachability A→B and save closure");
        System.out.println(" 3) Display full network graph");
        System.out.println(" 4) Find maintenance route");
        System.out.println(" 0) Exit");
        System.out.print("Select an option: ");
        return scanner.nextInt();
    }

    /**
     * Entry point for standalone testing.
     */
    public static void main(String[] args) {
        MenuUI menu = new MenuUI(false);
        int choice = menu.showMenu();
        System.out.println("You selected: " + choice);
    }
}
