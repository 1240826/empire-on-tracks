// SimulationTest.java
package pt.ipp.isep.dei;

import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.Date;
import pt.ipp.isep.dei.domain.Industry;
import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;
import pt.ipp.isep.dei.repository.ScenarioRepository;

import java.time.LocalDateTime;
import java.util.List;

public class SimulationTest {
    public static void main(String[] args) {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);
        Map map = new Map("Teste", 5, 5);

        // Estação
        Station st = new Station("ST1", "Central", new pt.ipp.isep.dei.domain.Location(1, 1), 5);
        map.addStation(st);

        // Indústria
        Industry ind = IndustryRepository.getInstance().getIndustryTemplate(0);
        map.addIndustry(ind, new pt.ipp.isep.dei.domain.Location(2, 2));

        // Locomotiva
        Locomotive loco = LocomotiveRepository.getInstance().getTemplate(0);
        map.addLocomotive(loco);

        // Rota: visita sempre a mesma estação
        loco.getRoute().clear();
        loco.getRoute().add(st);
        loco.getRoute().add(st);

        // Controller
        Date clock = new Date(start, 20L);
        SimulatorController controller = new SimulatorController(
                map,
                clock,
                new ScenarioRepository(),
                IndustryRepository.getInstance(),
                LocomotiveRepository.getInstance()
        );

        // Passo manual
        System.out.println(">> Executando stepWithEvents()");
        List<String> events = controller.stepWithEvents();
        events.forEach(e -> System.out.println("  * " + e));

        // Status final (note o uso de %s para tipos não-numéricos)
        System.out.println("\n>> Status final:");
        System.out.printf("Locomotiva %s carga atual: %.2f%n",
                loco.getId(),
                loco.getCurrentLoad()
        );
        System.out.printf("Estação %s tem carga: %s%n",
                st.getStationName(),
                st.getCargoes()    // já imprime a lista corretamente com %s
        );
        System.out.printf("Indústria %s estoque: %.2f%n",
                ind.getIndustryName(),
                ind.getCargo().getQuantity()
        );

        controller.shutdown();
    }
}
