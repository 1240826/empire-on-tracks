package pt.ipp.isep.dei.domain;

/**
 * Represents different types of train stations with distinct configurations.
 */
public enum StationType {
    DEPOT("Depot 3x3", 3, 3, 2),
    Station_4x4("Station 4x4", 4, 4, 4),
    TERMINAL_5x5("Terminal 5x5", 5, 5, 6);

    private final String label;
    private final int width;
    private final int height;
    private final int numTracks;

    StationType(String label, int width, int height, int numTracks) {
        this.label = label;
        this.width = width;
        this.height = height;
        this.numTracks = numTracks;
    }

    public String getLabel() {
        return label;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getNumTracks() {
        return numTracks;
    }

    @Override
    public String toString() {
        return label + " (" + width + "x" + height + ", Tracks: " + numTracks + ")";
    }
}
