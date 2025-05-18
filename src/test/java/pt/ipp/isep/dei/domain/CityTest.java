package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    class CityTest {
        private static final String VALID_NAME = "TestCity";
        private static final int CENTER_X = 5;
        private static final int CENTER_Y = 10;
        private static final int BLOCK_COUNT = 3;

        private City city;

        @BeforeEach
        void setUp() {
            city = City.createCity(VALID_NAME, CENTER_X, CENTER_Y, BLOCK_COUNT);
        }

        @Test
        void createCity_withValidParameters_setsFields() {
            assertEquals(VALID_NAME, city.getName());
            assertEquals(CENTER_X, city.getCenterX());
            assertEquals(CENTER_Y, city.getCenterY());
            assertEquals(BLOCK_COUNT, city.getHouseBlocksCount());
            assertNotNull(city.getCenter());
            assertEquals(CENTER_X, city.getCenter().getX());
            assertEquals(CENTER_Y, city.getCenter().getY());
            assertTrue(city.getHouseBlocks().isEmpty());
        }

        @Test
        void createCity_withNullName_throwsException() {
            Executable exec = () -> City.createCity(null, 0, 0, 1);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, exec);
            assertTrue(e.getMessage().contains("Invalid city name"));
        }

        @Test
        void createCity_withEmptyName_throwsException() {
            Executable exec = () -> City.createCity("  ", 0, 0, 1);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, exec);
            assertTrue(e.getMessage().contains("Invalid city name"));
        }

        @Test
        void createCity_withNonPositiveBlocks_throwsException() {
            Executable execZero = () -> City.createCity(VALID_NAME, 0, 0, 0);
            Executable execNegative = () -> City.createCity(VALID_NAME, 0, 0, -5);
            assertThrows(IllegalArgumentException.class, execZero);
            assertThrows(IllegalArgumentException.class, execNegative);
        }

        @Test
        void assignHouseBlocksManual_withValidPositions_assignsBlocks() {
            List<Location> positions = new ArrayList<>();
            positions.add(new Location(1, 1));
            positions.add(new Location(2, 2));
            positions.add(new Location(3, 3));

            city.assignHouseBlocksManually(positions);
            List<HouseBlock> blocks = city.getHouseBlocks();
            assertEquals(BLOCK_COUNT, blocks.size());
            for (int i = 0; i < BLOCK_COUNT; i++) {
                assertEquals(positions.get(i), blocks.get(i).getPosition());
            }
        }

        @Test
        void assignHouseBlocksManual_withNullList_throwsException() {
            assertThrows(NullPointerException.class,
                    () -> city.assignHouseBlocksManually(null));
        }

        @Test
        void assignHouseBlocksManual_withWrongSize_throwsException() {
            List<Location> tooFew = new ArrayList<>();
            tooFew.add(new Location(0, 0));
            Executable exec = () -> city.assignHouseBlocksManually(tooFew);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, exec);
            assertTrue(e.getMessage().contains("must match house block count"));
        }

        @Test
        void assignHouseBlocksAutomatic_generatesCorrectNumberOfBlocks() {
            city.assignHouseBlocksAutomatic();
            List<HouseBlock> blocks = city.getHouseBlocks();
            assertEquals(BLOCK_COUNT, blocks.size());
            // Ensure blocks are placed around center; positions differ but not null
            for (HouseBlock hb : blocks) {
                assertNotNull(hb.getPosition());
            }
        }

        @Test
        void getHouseBlocks_returnsImmutableList() {
            city.assignHouseBlocksAutomatic();
            List<HouseBlock> blocks = city.getHouseBlocks();
            // modify returned list
            blocks.clear();
            // original remains unchanged
            assertEquals(BLOCK_COUNT, city.getHouseBlocks().size());
        }
    }
