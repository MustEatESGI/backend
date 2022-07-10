package fr.esgi.musteat.backend.mealordered.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MealOrderedValidatorTest {

    private static MealOrderedValidator mealOrderedValidator;
    private static String name;
    private static Long price;

    @BeforeAll
    static void setup() {
        mealOrderedValidator = new MealOrderedValidator();
        name = "name";
        price = 10_00L;
    }

    @Test
    void should_be_valid() {
        MealOrdered mealOrdered = new MealOrdered(name, price, null);
        assertThatNoException().isThrownBy(() -> mealOrderedValidator.validate(mealOrdered));
    }

    @Test
    void should_be_invalid_when_meal_ordered_is_null() {
        assertThatThrownBy(() -> mealOrderedValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered is null");
    }

    @Test
    void should_be_invalid_when_name_is_null() {
        MealOrdered mealOrdered = new MealOrdered(null, price, null);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered name is null or empty");
    }

    @Test
    void should_be_invalid_when_name_is_empty() {
        MealOrdered mealOrdered = new MealOrdered("", price, null);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered name is null or empty");
    }

    @Test
    void should_be_invalid_when_price_is_null() {
        MealOrdered mealOrdered = new MealOrdered(name, null, null);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered price is null or negative");
    }

    @Test
    void should_be_invalid_when_price_is_negative() {
        MealOrdered mealOrdered = new MealOrdered(name, -1L, null);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered price is null or negative");
    }
}
