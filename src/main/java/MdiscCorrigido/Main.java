package MdiscCorrigido;

import MdiscCorrigido.UI.RailwayUI;

public class Main {
    public static void main(String[] args) {
        // 1) Configure o GraphStream para usar Swing (antes de qualquer display())
        System.setProperty("org.graphstream.ui", "swing");
        System.setProperty(
                "org.graphstream.ui.renderer",
                "org.graphstream.ui.j2dviewer.J2DGraphRenderer"
        );

        // 2) Delegue toda a UI à RailwayUI
        RailwayUI.main(args);
    }
}
