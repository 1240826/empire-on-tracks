package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.domain.StationType;    // adiciona aqui StationType

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StationTest {

    private Station station;
    private Date fixedDate;

    @BeforeEach
    public void setUp() {
        Location location = new Location(5, 5);
        // em vez de (id, name, location, 2) usas o tipo 5×5
        station = new Station("S1", "Main", location, StationType.TERMINAL_5x5);
        station.setBuildings(new ArrayList<>());
        fixedDate = new Date();
    }

    @Test
    public void testHasBuildingWhenBuildingExists() {
        station.addBuilding(new Building("POST_OFFICE", fixedDate, true));
        assertTrue(station.hasBuilding("POST_OFFICE"));
    }

    @Test
    public void testHasBuildingFalseWhenInactive() {
        station.addBuilding(new Building("SILO", fixedDate, false));
        assertFalse(station.hasBuilding("SILO"));
    }

    @Test
    public void testRemoveBuildingEliminatesAllOfType() {
        station.addBuilding(new Building("TELEGRAPH", fixedDate, true));
        station.addBuilding(new Building("TELEGRAPH", fixedDate, true));
        station.removeBuilding("TELEGRAPH");
        assertFalse(station.hasBuilding("TELEGRAPH"));
    }

    @Test
    public void testCollectNearbyIndustriesCollectsCorrectly() {
        Industry industry = new DummyIndustry(
                "CoalMine",
                new Location(6, 5),
                new Cargo(CargoType.COAL, 10)
        );
        List<Industry> industries = List.of(industry);

        station.collectNearbyIndustries(industries);

        assertEquals(10.0, station.getCargoQuantity(CargoType.COAL), 0.01);
        assertEquals(0.0, industry.getCargo().getQuantity(), 0.01);
    }
}

