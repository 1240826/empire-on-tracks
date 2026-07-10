package pt.ipp.isep.dei.example;

import org.junit.jupiter.api.Test;
import pt.ipp.isep.dei.repository.Repositories;

import static org.junit.jupiter.api.Assertions.*;

class RepositoriesTest {

    @Test
    void testGetInstance() {
        Repositories instance = Repositories.getInstance();
        assertNotNull(instance);
    }
}

