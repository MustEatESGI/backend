package fr.esgi.musteat.backend.meal.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MealValidatorTest {

    private static MealValidator mealValidator;
    private static String name;
    private static Long price;

    @BeforeAll
    static void setup() {
        mealValidator = new MealValidator();
        name = "name";
        price = 10_00L;
    }

    @Test
    void should_be_valid() {
        Meal meal = new Meal(name, price, null);
        assertThatNoException().isThrownBy(() -> mealValidator.validate(meal));
    }

    @Test
    void should_be_invalid_when_meal_is_null() {
        assertThatThrownBy(() -> mealValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Meal is null");
    }

    @Test
    void should_be_invalid_when_name_is_null() {
        Meal meal = new Meal(null, price, null);
        assertThatThrownBy(() -> mealValidator.validate(meal)).isInstanceOf(IllegalArgumentException.class).hasMessage("Meal name is null or empty");
    }

    @Test
    void should_be_invalid_when_name_is_empty() {
        Meal meal = new Meal("", price, null);
        assertThatThrownBy(() -> mealValidator.validate(meal)).isInstanceOf(IllegalArgumentException.class).hasMessage("Meal name is null or empty");
    }

    @Test
    void should_be_invalid_when_price_is_null() {
        Meal meal = new Meal(name, null, null);
        assertThatThrownBy(() -> mealValidator.validate(meal)).isInstanceOf(IllegalArgumentException.class).hasMessage("Meal price is null or negative");
    }

    @Test
    void should_be_invalid_when_price_is_negative() {
        Meal meal = new Meal(name, -1L, null);
        assertThatThrownBy(() -> mealValidator.validate(meal)).isInstanceOf(IllegalArgumentException.class).hasMessage("Meal price is null or negative");
    }
}
