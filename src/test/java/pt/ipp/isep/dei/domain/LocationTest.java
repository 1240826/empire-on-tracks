package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    @Test
    void constructor_setsCoordinatesCorrectly() {
        Location loc = new Location(7, -3);
        assertEquals(7, loc.getX());
        assertEquals(-3, loc.getY());
        assertEquals("(7, -3)", loc.toString());
    }

    @Test
    void toString_formatIsCorrect() {
        Location loc = new Location(0, 0);
        assertEquals("(0, 0)", loc.toString());
    }

    @Test
    void computeBlockPosition_withNullCenter_throwsException() {
        assertThrows(NullPointerException.class,
                () -> Location.computeBlockPosition(null));
    }

    @Test
    void computeBlockPosition_offsetsWithinReasonableBounds() {
        Location center = new Location(100, 200);
        for (int i = 0; i < 20; i++) {
            Location loc = Location.computeBlockPosition(center);
            assertNotNull(loc);
            int dx = Math.abs(loc.getX() - center.getX());
            int dy = Math.abs(loc.getY() - center.getY());
            // STD_DEV = 10, so 5×STD_DEV = 50 covers >99% under normal distribution
            assertTrue(dx <= 50, "dx too large: " + dx);
            assertTrue(dy <= 50, "dy too large: " + dy);
        }
    }
}
