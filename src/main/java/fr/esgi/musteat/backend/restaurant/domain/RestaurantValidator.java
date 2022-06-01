package fr.esgi.musteat.backend.restaurant.domain;

import fr.esgi.musteat.backend.kernel.Validator;

public class RestaurantValidator implements Validator<Restaurant> {

    @Override
    public void validate(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant is null");
        }

        if (restaurant.getName() == null || restaurant.getName().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is null or empty");
        }
    }
}
