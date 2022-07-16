package fr.esgi.musteat.backend.meal.domain;

import fr.esgi.musteat.backend.kernel.EntityTest;
import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MealTest extends EntityTest<Meal> {

    public MealTest() {
        super(new Meal(1L, "meal", 1L, null), new Meal(1L, "meal", 1L, null));
    }

    @Test
    void testToString() {
        Meal meal = new Meal(1L, "meal", 1L, null);
        assertEquals("Meal{id=1, name='meal', price=1, restaurant=null}", meal.toString());
    }
}
