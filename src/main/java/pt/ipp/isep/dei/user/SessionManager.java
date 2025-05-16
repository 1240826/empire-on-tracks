package pt.ipp.isep.dei.user;

import pt.ipp.isep.dei.repository.ApplicationSession;

/**
 * Manages the lifecycle of user sessions including start and end operations.
 */
public class SessionManager {

    /**
     * Starts a new session for the specified user.
     *
     * @param user the user to start a session for
     */
    public void startSession(User user) {
        UserSession session = new UserSession(user);
        ApplicationSession.getInstance().setCurrentSession(session);
    }

    /**
     * Ends the current user session.
     */
    public void endSession() {
        ApplicationSession.getInstance().setCurrentSession(null);
    }
}

