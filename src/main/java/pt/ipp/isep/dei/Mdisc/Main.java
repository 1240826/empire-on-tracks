// src/main/java/pt/ipp/isep/dei/Mdisc/Main.java
package pt.ipp.isep.dei.Mdisc;

import pt.ipp.isep.dei.Mdisc.controller.AppController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1) Lê os caminhos dos CSV
        System.out.print("Caminho do ficheiro de estações: ");
        String stationsFile = sc.nextLine().trim();
        System.out.print("Caminho do ficheiro de linhas: ");
        String linesFile = sc.nextLine().trim();

        // 2) Pergunta o tipo de comboio
        System.out.println("\nTipo de comboio:");
        System.out.println("1) Elétrico");
        System.out.println("2) Diesel");
        System.out.println("3) Steam");
        System.out.print("Escolha: ");
        int tipo = sc.nextInt();
        sc.nextLine();
        // Se for elétrico, só eletrificadas = true;
        // para os outros, false.
        boolean onlyElectrified = (tipo == 1);

        // 3) Configurações do GraphStream
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // 4) Inicia o controller com esse flag inicial
        new AppController(stationsFile, linesFile, onlyElectrified).start();

        sc.close();
    }
}
