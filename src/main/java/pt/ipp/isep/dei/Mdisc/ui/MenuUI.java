package pt.ipp.isep.dei.Mdisc.ui;

import java.util.Scanner;

public class MenuUI {
    private boolean onlyElectrified;

    public MenuUI(boolean onlyElectrified) {
        this.onlyElectrified = onlyElectrified;
    }

    public int showMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║                 OPÇÕES                 ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.printf("║ 1) Só eletrificadas: %-6s             ║%n",
                onlyElectrified ? "SIM" : "NÃO");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 2) Exportar matriz (matrix.txt)        ║");
        System.out.println("║ 3) Verificar alcance A→B e salvar      ║");
        System.out.println("║    fecho (closure.txt)                 ║");
        System.out.println("║ 4) Visualizar grafo completo           ║");
        System.out.println("║ 5) Checar rota de manutenção           ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║ 0) Sair                                ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.print("Escolha: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    public void updateOnlyElectrified(boolean onlyElectrified) {
        this.onlyElectrified = onlyElectrified;
    }
}