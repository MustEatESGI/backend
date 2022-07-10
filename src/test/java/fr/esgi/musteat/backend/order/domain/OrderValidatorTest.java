package fr.esgi.musteat.backend.order.domain;

import fr.esgi.musteat.backend.location.domain.Location;
import fr.esgi.musteat.backend.restaurant.domain.Restaurant;
import fr.esgi.musteat.backend.user.domain.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderValidatorTest {

    private static OrderValidator orderValidator;
    private static LocalDateTime orderDate;
    private static User user;
    private static Restaurant restaurant;

    @BeforeAll
    public static void setUp() {
        orderValidator = new OrderValidator();
        orderDate = LocalDateTime.now();
        Location location = new Location(10.0, 10.0);
        user = new User("name", "Password1", location);
        restaurant = new Restaurant("name", location);
    }

    @Test
    public void should_be_valid() {
        Order order = new Order(orderDate, user, restaurant);
        orderValidator.validate(order);
    }

    @Test
    public void should_be_invalid_when_order_is_null() {
        assertThatThrownBy(() -> orderValidator.validate(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order is null");
    }

    @Test
    public void should_be_invalid_when_order_date_is_null() {
        Order order = new Order(null, user, restaurant);
        assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order date is null");
    }

    @Test
    public void should_be_invalid_when_order_date_is_in_the_future() {
        Order order = new Order(LocalDateTime.now().plusDays(1), user, restaurant);
        assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order date is in the future");
    }

    @Test
    public void should_be_invalid_when_user_is_null() {
        Order order = new Order(orderDate, null, restaurant);
        assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order user is null");
    }

    @Test
    public void should_be_invalid_when_restaurant_is_null() {
        Order order = new Order(orderDate, user, null);
        assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class).hasMessage("Order restaurant is null");
    }
}
