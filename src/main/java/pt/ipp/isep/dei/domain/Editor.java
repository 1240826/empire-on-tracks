package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.user.User;

public class Editor extends User {
    public Editor(String username, String password) {
        super(username, password);  // Call the constructor of the base class
    }

    // Additional methods specific to Editor can be added here
}
