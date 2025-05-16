package pt.ipp.isep.dei.repository;

import pt.isep.lei.esoft.auth.UserSession;

/**
 * Singleton that manages the application-wide current user session.
 */
public class ApplicationSession {
    private static ApplicationSession instance;
    private UserSession currentSession;

    /**
     * Private constructor for singleton pattern.
     */
    public ApplicationSession() {}

    /**
     * Retrieves the singleton instance of ApplicationSession.
     *
     * @return the ApplicationSession instance
     */
    public static synchronized ApplicationSession getInstance() {
        if (instance == null) {
            instance = new ApplicationSession();
        }
        return instance;
    }

    /**
     * Returns the current active user session.
     *
     * @return current UserSession or null if no session exists
     */
    public UserSession getCurrentSession() {
        return currentSession;
    }

    /**
     * Sets the current active user session.
     * Intended for use by SessionManager only.
     *
     * @param session the UserSession to set as current
     */
    void setCurrentSession(UserSession session) {
        this.currentSession = session;
    }
}
