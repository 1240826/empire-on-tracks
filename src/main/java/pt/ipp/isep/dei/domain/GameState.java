package pt.ipp.isep.dei.domain;

public class GameState {
    private static GameState instance;

    private GameState() {
        // private constructor
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void save() {
        System.out.println("[GameState] Game saved successfully.");
    }
}
