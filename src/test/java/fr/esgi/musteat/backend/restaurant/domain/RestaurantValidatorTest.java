package fr.esgi.musteat.backend.restaurant.domain;

import fr.esgi.musteat.backend.location.domain.Location;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantValidatorTest {

    private static RestaurantValidator restaurantValidator;
    private static String name;
    private static Location location;

    @BeforeAll
    static void setup() {
        restaurantValidator = new RestaurantValidator();
        name = "name";
        location = new Location(10.0, 10.0);
    }

    @Test
    void should_be_valid() {
        Restaurant restaurant = new Restaurant(name, location);
        assertThatNoException().isThrownBy(() -> restaurantValidator.validate(restaurant));
    }

    @Test
    void should_be_invalid_when_restaurant_is_null() {
        assertThatThrownBy(() -> restaurantValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Restaurant is null");
    }

    @Test
    void should_be_invalid_when_name_is_null() {
        Restaurant restaurant = new Restaurant(null, location);
        assertThatThrownBy(() -> restaurantValidator.validate(restaurant)).isInstanceOf(IllegalArgumentException.class).hasMessage("Restaurant name is null or empty");
    }

    @Test
    void should_be_invalid_when_name_is_empty() {
        Restaurant restaurant = new Restaurant("", location);
        assertThatThrownBy(() -> restaurantValidator.validate(restaurant)).isInstanceOf(IllegalArgumentException.class).hasMessage("Restaurant name is null or empty");
    }

    @Test
    void should_be_invalid_when_location_is_null() {
        Restaurant restaurant = new Restaurant(name, null);
        assertThatThrownBy(() -> restaurantValidator.validate(restaurant)).isInstanceOf(IllegalArgumentException.class).hasMessage("Restaurant location is null");
    }
}
