package pt.ipp.isep.dei.ui.console;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.controller.CreateMapController;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class CreatMapUITest {

    @Test
    public void create() {
        String simulatedInput = "MyMap\n-10\n15\n";
        InputStream originalIn = System.in; // salva o original

        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes())); // substitui System.in

        CreatMapUI ui = new CreatMapUI();
        CreateMapController controller = new CreateMapController(); // ou um mock
        ui.setCreateMapController(controller);


        ui.create(); // executa com a entrada simulada

        System.setIn(originalIn);
    }

}