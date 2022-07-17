package fr.esgi.musteat.backend.mealordered.domain;

import fr.esgi.musteat.backend.kernel.EntityTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MealOrderedTest extends EntityTest<MealOrdered> {

    public MealOrderedTest() {
        super(new MealOrdered(1L, "name", 1L, null), new MealOrdered(1L, "name", 1L, null));
    }

    @Test
    void testToString() {
        MealOrdered mealOrdered = new MealOrdered(1L, "name", 1L, null);
        assertEquals("MealOrdered{id=1, name='name', price=1, order=null}", mealOrdered.toString());
    }
}
