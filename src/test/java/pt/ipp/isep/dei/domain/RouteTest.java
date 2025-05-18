package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RouteTest {


    @Test
    void constructor_throwsExceptionIfStopsIsNull() {
        assertThrows(NullPointerException.class, () -> new Route(null));
    }

}
