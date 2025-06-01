package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static pt.ipp.isep.dei.domain.CargoType.*;

class CarriageTest {

    @Test
    void testCarriageConstructorAndGetters() {
        Carriage cargoCarriage = new Carriage(STEEL, 50);
        Carriage passengerCarriage = new Carriage(PASSENGERS, 100);


    }


    @Test
    void getCapacity_returnsCapacity() {
        Carriage c = new Carriage(MAIL, 10);
        assertEquals(10, c.getCapacity());
    }
}
