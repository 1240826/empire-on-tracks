package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.user.PasswordValidator;
import pt.ipp.isep.dei.user.SessionManager;
import pt.ipp.isep.dei.user.User;

/**
 * Manages user login operations, including authentication and session creation.
 */
public class LoginController {
    private PasswordValidator passwordValidator = new PasswordValidator();

    /**
     * Authenticates a user by username and password.
     * On successful authentication, creates a user session.
     *
     * @param username the username provided
     * @param password the password provided
     * @return true if authentication succeeds, false otherwise
     */
    public boolean authenticateUser(String username, String password) {
        if (username == null || password == null) return false;
        if (!passwordValidator.validate(password)) return false;

        // In a real scenario, verify user credentials against a data store.
        SessionManager sessionManager = new SessionManager();
        User user = new User(username, password);
        sessionManager.startSession(user);

        return true;
    }
}

