package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioTest {
    private Scenario scenario;
    private static final LocalDateTime NOW = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        scenario = new Scenario(NOW);
    }

    @Test
    void constructor_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> new Scenario(null));
    }

    @Test
    void getTriggerDate_returnsSetValue() {
        assertEquals(NOW, scenario.getTriggerDate());
    }

    @Test
    void setTriggerDate_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> scenario.setTriggerDate(null));
    }

    @Test
    void setTriggerDate_updatesValue() {
        LocalDateTime later = NOW.plusDays(1);
        scenario.setTriggerDate(later);
        assertEquals(later, scenario.getTriggerDate());
    }

    @Test
    void setPeriod_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> scenario.setPeriod(null));
    }

    @Test
    void setPeriod_andGetPeriod() {
        Period p = new Period(1, 5);
        scenario.setPeriod(p);
        assertEquals(p, scenario.getPeriod());
    }

    @Test
    void addLocomotive_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> scenario.addLocomotive(null));
    }

    @Test
    void addLocomotive_andGetLocomotives_returnsImmutableCopy() {
        Locomotive loco = new Locomotive("id","type",0,false,0,0,0,0,0,0);
        scenario.addLocomotive(loco);
        List<Locomotive> list = scenario.getLocomotives();
        assertEquals(1, list.size());
        list.clear();
        assertEquals(1, scenario.getLocomotives().size());
    }

    @Test
    void addCargo_withNull_throwsException() {
        assertThrows(NullPointerException.class, () -> scenario.addCargo(null));
    }

    @Test
    void addCargo_andGetCargos_returnsImmutableCopy() {
        Cargo cargo = new Cargo(CargoType.COAL, 0);
        scenario.addCargo(cargo);
        List<Cargo> list = scenario.getCargos();
        assertEquals(1, list.size());
        list.add(new Cargo(CargoType.OIL, 1));
        assertEquals(1, scenario.getCargos().size());
    }

    @Test
    void setIndustryProductionFrequency_withInvalidLow_throws() {
        assertThrows(IllegalArgumentException.class, () -> scenario.setIndustryProductionFrequency(-1));
    }

    @Test
    void setIndustryProductionFrequency_withInvalidHigh_throws() {
        assertThrows(IllegalArgumentException.class, () -> scenario.setIndustryProductionFrequency(101));
    }

    @Test
    void setIndustryProductionFrequency_andGet() {
        scenario.setIndustryProductionFrequency(50);
        assertEquals(50, scenario.getIndustryProductionFrequency());
    }

    @Test
    void applyAll_withNullMap_throwsException() {
        List<Locomotive> list = new ArrayList<>();
        assertThrows(NullPointerException.class, () -> scenario.applyAll(null, list));
    }

    @Test
    void applyAll_withNullLocos_throwsException() {
        Map map = new Map();
        assertThrows(NullPointerException.class, () -> scenario.applyAll(map, null));
    }

    @Test
    void applyAll_addsLocomotivesToMap() {
        // stub map
        class TestMap extends Map {
            List<Locomotive> added = new ArrayList<>();
            @Override public void addLocomotive(Locomotive loco) {
                added.add(loco);
            }
        }
        TestMap map = new TestMap();
        Locomotive loco1 = new Locomotive("id","type",0,false,0,0,0,0,0,0);
        Locomotive loco2 = new Locomotive("id2","type2",0,false,0,0,0,0,0,0);
        List<Locomotive> locos = List.of(loco1, loco2);
        scenario.applyAll(map, locos);
        assertEquals(2, map.added.size());
        assertTrue(map.added.contains(loco1));
        assertTrue(map.added.contains(loco2));
    }
}
