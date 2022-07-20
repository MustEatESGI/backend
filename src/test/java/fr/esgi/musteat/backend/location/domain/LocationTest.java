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
        // GPS position of Brest (FR) : 48.3764, -4.4865
        Location location = new Location(1L, 48.3764, -4.4865);
        // GPS position of Lille (FR) : 50.6332, 3.0668
        Location userLocation = new Location(2L, 50.6332, 3.0668);
        // Distance between these two cities is 600km
        assertEquals(600_000, location.getDistance(userLocation));
    }
}
