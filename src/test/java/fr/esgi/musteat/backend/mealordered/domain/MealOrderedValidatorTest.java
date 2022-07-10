package fr.esgi.musteat.backend.mealordered.domain;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.user.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MealOrderedValidatorTest {

    private static MealOrderedValidator mealOrderedValidator;
    private static String name;
    private static Long price;
    private static Order order;

    @BeforeAll
    static void setup() {
        mealOrderedValidator = new MealOrderedValidator();
        name = "name";
        price = 10_00L;
        Location location = new Location(10.0, 10.0);
        Restaurant restaurant = new Restaurant("name", location);
        User user = new User("name", "Password1", location);
        order = new Order(LocalDateTime.now(), user, restaurant);
    }

    @Test
    void should_be_valid() {
        MealOrdered mealOrdered = new MealOrdered(name, price, order);
        assertThatNoException().isThrownBy(() -> mealOrderedValidator.validate(mealOrdered));
    }

    @Test
    void should_be_invalid_when_meal_ordered_is_null() {
        assertThatThrownBy(() -> mealOrderedValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered is null");
    }

    @Test
    void should_be_invalid_when_name_is_null() {
        MealOrdered mealOrdered = new MealOrdered(null, price, order);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered name is null or empty");
    }

    @Test
    void should_be_invalid_when_name_is_empty() {
        MealOrdered mealOrdered = new MealOrdered("", price, order);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered name is null or empty");
    }

    @Test
    void should_be_invalid_when_price_is_null() {
        MealOrdered mealOrdered = new MealOrdered(name, null, order);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered price is null or negative");
    }

    @Test
    void should_be_invalid_when_price_is_negative() {
        MealOrdered mealOrdered = new MealOrdered(name, -1L, order);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered price is null or negative");
    }

    @Test
    void should_be_invalid_when_order_is_null() {
        MealOrdered mealOrdered = new MealOrdered(name, price, null);
        assertThatThrownBy(() -> mealOrderedValidator.validate(mealOrdered)).isInstanceOf(IllegalArgumentException.class).hasMessage("MealOrdered order is null");
    }
}
