package MdiscCorrigido.UI;

import MdiscCorrigido.Controller.AppController;

import java.util.Scanner;

/**
 * Interface de Utilizador (UI) principal.
 * 1) Pergunta caminho para CSV de estações
 * 2) Pergunta caminho para CSV de linhas
 * 3) Pergunta tipo de comboio
 * 4) Instancia AppController e apresenta menu
 */
public class RailwayUI {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Insira o caminho para o ficheiro CSV de estações: ");
        String stationsPath = sc.nextLine().trim();

        System.out.print("Insira o caminho para o ficheiro CSV de linhas: ");
        String linesPath = sc.nextLine().trim();

        System.out.println("\nSelecione o tipo de comboio:");
        System.out.println("1) Electric");
        System.out.println("2) Diesel");
        System.out.println("3) Steam");
        System.out.print("Choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consumir newline
        boolean onlyElectrified = (choice == 1);

        AppController controller = new AppController(stationsPath, linesPath, onlyElectrified);
        controller.startInteractive();

        sc.close();
    }
}
