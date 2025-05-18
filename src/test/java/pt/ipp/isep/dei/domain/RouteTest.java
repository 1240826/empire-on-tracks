package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteTest {

    @Test
    void testConstructorAndGetter() {
        List<String> testList = List.of("Station A", "Station B");
        Route route = new Route(testList);
        assertEquals(testList, route.getStationCargoList());
    }

    @Test
    void testEmptyStationCargoList() {
        List<String> emptyList = List.of();
        Route route = new Route(emptyList);
        assertNotNull(route.getStationCargoList());
        assertTrue(route.getStationCargoList().isEmpty());
    }

    @Test
    void testNullStationCargoListThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Route(null);
        });
        assertEquals("stationCargoList cannot be null", exception.getMessage());
    }
}
