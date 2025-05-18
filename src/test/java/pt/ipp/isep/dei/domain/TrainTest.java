package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrainTest {

    @Test
    void testConstructorAndGetters() {
        Locomotive loco = new Locomotive("L1", "Steam", 8000.0, false, 1000, 1.0, 80, 1900, 300, 200, 10,  new Route(List.of("StationA,Steel", "StationB,Coal")));
        Carriage c1 = new Carriage("cargo", 50);
        Carriage c2 = new Carriage("passenger", 100);
        List<Carriage> carriages = List.of(c1, c2);
        List<String> cargoes = List.of("Coal", "Iron");

        Train train = new Train("Atlantic Express", loco, carriages, cargoes);

        assertEquals("Atlantic Express", train.getName());
        assertEquals("Steam", train.getLocomotiveType());
        assertEquals(cargoes, train.getCurrentCargoes());
        assertNull(train.getRoute());
    }

    @Test
    void testGetLocomotiveTypeNullLoco() {
        Train train = new Train("No Loco Train", null, List.of(), List.of());
        assertEquals("", train.getLocomotiveType());
    }

    @Test
    void testAssignAndGetRoute() {
        Train train = new Train("Test Train", null, List.of(), List.of());
        Route route = new Route(List.of("StationA,Steel", "StationB,Coal"));
        train.assignRoute(route);
        assertEquals(route, train.getRoute());
    }
}
