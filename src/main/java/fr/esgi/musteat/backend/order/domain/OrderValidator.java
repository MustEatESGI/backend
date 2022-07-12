package fr.esgi.musteat.backend.order.domain;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantValidator;
import fr.esgi.musteat.backend.user.domain.UserValidator;

import java.time.LocalDateTime;

public class OrderValidator implements Validator<Order> {

    private final UserValidator userValidator = new UserValidator();
    private final RestaurantValidator restaurantValidator = new RestaurantValidator();

    @Override
    public void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is null");
        }

        if (order.getOrderDate() == null) {
            throw new IllegalArgumentException("Order date is null");
        }

        if (order.getOrderDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Order date is in the future");
        }

        if (order.getUser() == null) {
            throw new IllegalArgumentException("Order user is null");
        }

        if (order.getRestaurant() == null) {
            throw new IllegalArgumentException("Order restaurant is null");
        }

        userValidator.validate(order.getUser());
        restaurantValidator.validate(order.getRestaurant());
    }
}
