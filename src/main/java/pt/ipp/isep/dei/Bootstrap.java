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


}