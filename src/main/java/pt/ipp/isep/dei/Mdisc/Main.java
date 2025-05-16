// src/main/java/org/example/Main.java
package pt.ipp.isep.dei.Mdisc;

import pt.ipp.isep.dei.Mdisc.controller.AppController;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Caminho do arquivo de estações: ");
        String stationsFile = sc.nextLine().trim();

        System.out.print("Caminho do arquivo de linhas: ");
        String linesFile = sc.nextLine().trim();

        // Configurações do GraphStream
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty("org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

        // Inicia o controller com os caminhos fornecidos
        new AppController(stationsFile, linesFile).start();

        sc.close();
    }
}
