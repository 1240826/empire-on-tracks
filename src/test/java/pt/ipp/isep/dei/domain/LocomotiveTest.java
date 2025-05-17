package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {

    @Test
    void testConstructorAndGetters() {
        Locomotive loco = new Locomotive(
                "L001", "Diesel", 10000, false,
                2200, 1.8, 120.0, 1965,
                1300.5, 800.0
        );

        assertEquals("L001", loco.getId());
        assertEquals("Diesel", loco.getType());
        assertEquals(10000, loco.getPrice());
        assertFalse(loco.requiresElectrification());
        assertEquals(2200, loco.getPower());
        assertEquals(1.8, loco.getAcceleration());
        assertEquals(120.0, loco.getTopSpeed());
        assertEquals(1965, loco.getStartYear());
        assertEquals(1300.5, loco.getFuelCostPerYear());
        assertEquals(800.0, loco.getMaintenancePerYear());
    }

    @Test
    void testToStringContainsAllFields() {
        Locomotive loco = new Locomotive(
                "L002", "Steam", 7500, false,
                1800, 1.2, 90.0, 1930,
                950.0, 600.0
        );
        String result = loco.toString();
        assertTrue(result.contains("L002"));
        assertTrue(result.contains("Steam"));
        assertTrue(result.contains("7500"));
        assertTrue(result.contains("false"));
        assertTrue(result.contains("1800"));
        assertTrue(result.contains("1.20")); // acceleration
        assertTrue(result.contains("90.00")); // top speed
        assertTrue(result.contains("1930"));
        assertTrue(result.contains("950.00"));
        assertTrue(result.contains("600.00"));
    }
}
