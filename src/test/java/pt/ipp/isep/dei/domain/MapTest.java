package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void addStation() {
        Map map = new Map("Test", 5, 5);
        Station s = new Station("Central", "CENTRAL", new Location(2, 2), StationType.TERMINAL_5x5);

        map.addStation(s);
        assertTrue(map.getStations().contains(s));
    }

    @Test
    void getStations() {
        Map map = new Map("Test", 5, 5);
        Station s = new Station("A", "A", new Location(1, 1), StationType.DEPOT);
        map.addStation(s);
        List<Station> result = map.getStations();
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getStationName());
    }

    @Test
    void getAllNamesStation() {
        Map map = new Map("Test", 5, 5);
        map.addStation(new Station("Lisboa", "LSB", new Location(1, 1), StationType.DEPOT));
        map.addStation(new Station("Porto", "PRT", new Location(2, 2), StationType.DEPOT));
        List<String> names = map.getAllNamesStation();
        assertTrue(names.contains("LSB"));
        assertTrue(names.contains("PRT"));
    }





    @Test
    void addIndustry() {
        Map map = new Map("Test", 5, 5);
        Industry industry = new ProducingIndustry();
        Location loc = new Location(2, 2);
        boolean added = map.addIndustry(industry, loc);
        assertTrue(added);
        assertEquals(1, map.getIndustries().size());
    }

    @Test
    void getIndustries() {
        Map map = new Map("Test", 5, 5);
        Industry industry = new ProducingIndustry();
        map.addIndustry(industry, new Location(1, 1));
        List<Industry> list = map.getIndustries();
        assertEquals(1, list.size());
    }

    @Test
    void addCity() {
        Map map = new Map("Test", 5, 5);
        City city = new City("Braga", 2, 2,10);
        boolean result = map.addCity(city);
        assertTrue(result);
    }

    @Test
    void getCities() {
        Map map = new Map("Test", 5, 5);
        map.addCity(new City("Aveiro", 1, 1,20));
        map.addCity(new City("Guimarães", 2, 2,10));
        List<String> cityNames = map.getCities();
        assertTrue(cityNames.contains("Aveiro"));
        assertTrue(cityNames.contains("Guimarães"));
    }

    @Test
    void isCityNameDuplicate() {
        Map map = new Map("Test", 5, 5);
        map.addCity(new City("Évora", 1, 1,10));
        assertTrue(map.isCityNameDuplicate("ÉVORA"));
        assertFalse(map.isCityNameDuplicate("Faro"));
    }

    @Test
    void isEmpty() {
        Map map = new Map("Test", 5, 5);
        assertTrue(map.isEmpty(2, 2));
        assertFalse(map.isEmpty(-1, 0));  // fora dos limites
        assertFalse(map.isEmpty(10, 10)); // fora dos limites
    }

    @Test
    void validateCoordinates() {
        Map map = new Map("Test", 5, 5);
        List<Location> valid = List.of(new Location(0, 0), new Location(4, 4));
        List<Location> invalid = List.of(new Location(-1, 0), new Location(5, 5));
        assertTrue(map.validateCoordinates(valid));
        assertFalse(map.validateCoordinates(invalid));
    }

    @Test
    void getWidth() {
        Map map = new Map("Test", 7, 4);
        assertEquals(7, map.getWidth());
    }

    @Test
    void getHeight() {
        Map map = new Map("Test", 7, 4);
        assertEquals(4, map.getHeight());
    }


    @Test
    void addLocomotive() {
        Map map = new Map("Test", 5, 5);
        Locomotive loco = new Locomotive("L1", "Steam", 1000, false,
                2000, 1.2, 120, 1900,
                100, 50);
        map.addLocomotive(loco);
        assertTrue(map.getLocomotives().contains(loco));
    }

    @Test
    void getLocomotives() {
        Map map = new Map("Test", 5, 5);
        Locomotive loco = new Locomotive("L1", "Steam", 1000, false,
                2000, 1.2, 120, 1900,
                100, 50);
        map.addLocomotive(loco);
        List<Locomotive> list = map.getLocomotives();
        assertEquals(1, list.size());
    }

    @Test
    void getName() {
        Map map = new Map("Simulador", 5, 5);
        assertEquals("Simulador", map.getName());
    }

    @Test
    void setName() {
        Map map = new Map("Mapa", 5, 5);
        map.setName("NovoMapa");
        assertEquals("NovoMapa", map.getName());
    }
}
