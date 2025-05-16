// src/main/java/pt/ipp/isep/dei/Mdisc/ui/MenuUI.java
package pt.ipp.isep.dei.Mdisc.ui;

import java.util.Scanner;

public class MenuUI {
    private final boolean onlyElectrified;

    public MenuUI(boolean onlyElectrified) {
        this.onlyElectrified = onlyElectrified;
    }

    public int showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                 OPÇÕES                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        // Mostra o status, sem opção de edição
        System.out.printf("║ Só eletrificadas: %-6s             ║%n",
                onlyElectrified ? "SIM" : "NÃO");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 1) Exportar matriz (matrix.txt)        ║");
        System.out.println("║ 2) Verificar alcance A→B e salvar      ║");
        System.out.println("║    fecho (closure.txt)                 ║");
        System.out.println("║ 3) Visualizar grafo completo           ║");
        System.out.println("║ 4) Checar rota de manutenção           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 0) Sair                                ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Escolha: ");
        return new Scanner(System.in).nextInt();
    }
}
