package pt.ipp.isep.dei;

import pt.ipp.isep.dei.controller.AuthenticationController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.IndustryRepository;
import pt.ipp.isep.dei.repository.LocomotiveRepository;
import pt.ipp.isep.dei.repository.ScenarioRepository;
import pt.ipp.isep.dei.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bootstraps application data: task categories, organization, and initial game player.
 */
public class Bootstrap implements Runnable {

    private final AuthenticationController authCtrl = new AuthenticationController();

    @Override
    public void run() {

        addPlayerUser();
    }



    private void addPlayerUser() {
        // Register a single game player with valid password
        String username = "player1";
        String password = "ABC12XY"; // 7 chars: 3 uppercase, 2 digits
        User player = authCtrl.register(username, password);
        System.out.printf("Bootstrapped player → Username=%s%n", player.getUsername());
    }

    public static class SimulationTest {
        public static void main(String[] args) {
            // 1) Data de início
            LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0);

            // 2) Criar mapa 5×5
            Map map = new Map("Teste", 5, 5);

            // 3) Adicionar uma estação
            Station st = new Station("ST1", "Central", new pt.ipp.isep.dei.domain.Location(1, 1), 5);
            map.addStation(st);

            // 4) Adicionar uma indústria
            Industry ind = IndustryRepository.getInstance().getIndustryTemplate(0);
            map.addIndustry(ind, new pt.ipp.isep.dei.domain.Location(2, 2));

            // 5) Criar locomotiva e adicionar ao mapa
            Locomotive loco = LocomotiveRepository.getInstance().getTemplate(0);
            map.addLocomotive(loco);

            // 6) Definir rota: Central → Central (para forçar visita)
            loco.getRoute().clear();
            loco.getRoute().add(st);
            loco.getRoute().add(st);

            // 7) Configurar controller
            Date clock = new Date(start, 20L);
            SimulatorController controller = new SimulatorController(
                    map,
                    clock,
                    new ScenarioRepository(),
                    IndustryRepository.getInstance(),
                    LocomotiveRepository.getInstance()
            );

            // 8) Executar um passo manual
            System.out.println(">> Executando stepWithEvents()");
            List<String> events = controller.stepWithEvents();
            events.forEach(e -> System.out.println("  * " + e));

            // 9) Imprimir status pós-step
            System.out.println("\n>> Status final:");
            System.out.printf("Locomotiva %s carga atual: %.2f%n",
                    loco.getId(),
                    loco.getCurrentLoad()
            );
            System.out.printf("Estação %s tem carga: %s%n",
                    st.getStationName(),
                    st.getCargoes()
            );
            System.out.printf("Indústria %s estoque: %.2f%n",
                    ind.getIndustryName(),
                    ind.getCargo().getQuantity()
            );

            // 10) Finalizar
            controller.shutdown();
        }
    }
}