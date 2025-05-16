package pt.ipp.isep.dei.user;

/**
 * Represents a user in the system with a username and password.
 */
public class User {
    private String username;
    private String password;

    /**
     * Creates a new user with the given username and password.
     *
     * @param username the user's username
     * @param password the user's password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username of this user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}