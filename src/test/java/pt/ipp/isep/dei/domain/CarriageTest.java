package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarriageTest {

    @Test
    void testCarriageConstructorAndGetters() {
        Carriage cargoCarriage = new Carriage("cargo", 50);
        Carriage passengerCarriage = new Carriage("passenger", 100);

        // Test type and capacity for cargo
        assertEquals("cargo", cargoCarriage.getType());
        assertEquals(50, cargoCarriage.getCapacity());

        // Test type and capacity for passenger
        assertEquals("passenger", passengerCarriage.getType());
        assertEquals(100, passengerCarriage.getCapacity());
    }

}
