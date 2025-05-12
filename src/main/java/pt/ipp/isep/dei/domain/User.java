package pt.ipp.isep.dei.domain;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Validate that the username is not null or empty.
     */
    public boolean validateUsername() {
        return username != null && !username.isEmpty();
    }

    /**
     * Validate password format:
     * - Exactly 7 characters
     * - 3 uppercase letters
     * - 2 digits
     */
    public boolean validatePassword() {
        if (password == null || password.length() != 7) {
            return false;
        }

        int upperCaseCount = 0;
        int digitCount = 0;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCaseCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            }
        }

        return upperCaseCount == 3 && digitCount == 2;
    }
}

