package fr.esgi.musteat.backend.restaurant.domain;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.location.domain.LocationValidator;

public class RestaurantValidator implements Validator<Restaurant> {

    private final LocationValidator locationValidator = new LocationValidator();

    @Override
    public void validate(Restaurant restaurant) {
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant is null");
        }

        if (restaurant.getName() == null || restaurant.getName().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is null or empty");
        }

        if (restaurant.getLocation() == null) {
            throw new IllegalArgumentException("Restaurant location is null");
        }

        locationValidator.validate(restaurant.getLocation());
    }
}
