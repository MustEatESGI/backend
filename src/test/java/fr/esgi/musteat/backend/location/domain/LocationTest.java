package fr.esgi.musteat.backend.location.domain;

import fr.esgi.musteat.backend.kernel.EntityTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationTest extends EntityTest<Location> {

    public LocationTest() {
        super(new Location(1L, 1.0, 1.0), new Location(1L, 1.0, 1.0));
    }

    @Test
    void testToString() {
        Location location = new Location(1L, 1.0, 1.0);
        assertEquals("Location{id=1, latitude=1.0, longitude=1.0}", location.toString());
    }

    @Test
    void test_get_distance() {
        Location location = new Location(1L, 1.0, 1.0);
        Location userLocation = new Location(2L, 10.0, -20.0);
        assertEquals(2284.0, location.getDistance(userLocation));
    }
}
