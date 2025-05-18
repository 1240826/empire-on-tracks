package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IndustryTest {

    private Industry industry;

    @BeforeEach
    void setUp() {
        // Implementação mínima de Industry para testes
        industry = new Industry() {
            private Location location;
            private final Cargo cargo = new Cargo(CargoType.COAL, 0);

            @Override
            public String getIndustryName() {
                return "Mock Industry";
            }

            @Override
            public int getIndustryCost() {
                return 100;
            }

            @Override
            public double getProduction() {
                return 50.0;
            }

            @Override
            public double getDemand() {
                return 0.0;
            }

            @Override
            public void setLocation(Location location) {
                this.location = location;
            }

            @Override
            public Location getLocation() {
                return location;
            }

            @Override
            public void produceCargo() {
                cargo.addQuantity(getProduction());
            }

            @Override
            public void consumeCargo(double quantity) {
                cargo.removeQuantity(quantity);
            }

            @Override
            public Cargo getCargo() {
                return cargo;
            }
        };
    }

    @Test
    void testGetIndustryName() {
        assertEquals("Mock Industry", industry.getIndustryName());
    }

    @Test
    void testGetIndustryCost() {
        assertEquals(100, industry.getIndustryCost());
    }

    @Test
    void testProductionAndCargoUpdate() {
        assertEquals(0.0, industry.getCargoQuantity());
        industry.produceCargo();
        assertEquals(50.0, industry.getCargoQuantity());
    }

    @Test
    void testConsumeCargo() {
        industry.produceCargo(); // 50
        industry.consumeCargo(20.0);
        assertEquals(30.0, industry.getCargoQuantity());
    }

    @Test
    void testSetAndGetLocation() {
        Location loc = new Location(3, 4);
        industry.setLocation(loc);
        assertEquals(loc, industry.getLocation());
    }

    @Test
    void testGetCargo() {
        Cargo cargo = industry.getCargo();
        assertNotNull(cargo);
        assertEquals(CargoType.COAL, cargo.getType());
    }
}
