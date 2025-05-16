package pt.ipp.isep.dei.domain;

import java.util.Random;

public class Location {
    private int x, y;
    private static final Random rand = new Random();
    private static final double STD_DEV = 10.0;

    public Location(int x, int y) {
        this.x = x; this.y = y;
    }
    public int getX() { return x; }
    public int getY() { return y; }

    /** retorna nova posição = centro + offset gaussiano */
    public static Location computeBlockPosition(Location center) {
        int dx = (int) Math.round(rand.nextGaussian() * STD_DEV);
        int dy = (int) Math.round(rand.nextGaussian() * STD_DEV);
        return new Location(center.x + dx, center.y + dy);
    }
}
