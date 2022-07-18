package fr.esgi.musteat.backend.restaurant.domain;

import fr.esgi.musteat.backend.kernel.EntityTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantTest extends EntityTest<Restaurant> {

    public RestaurantTest() {
        super(new Restaurant(1L, "name", null), new Restaurant(1L, "name", null));
    }

    @Test
    void testToString() {
        Restaurant restaurant = new Restaurant(1L, "name", null);
        assertEquals("Restaurant{id=1, name='name', location=null}", restaurant.toString());
    }
}
