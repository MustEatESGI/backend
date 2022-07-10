package fr.esgi.musteat.backend.meal.domain;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.restaurant.domain.RestaurantValidator;

public class MealValidator implements Validator<Meal> {

    private final RestaurantValidator restaurantValidator = new RestaurantValidator();

    @Override
    public void validate(Meal meal) {
        if (meal == null) {
            throw new IllegalArgumentException("Meal is null");
        }

        if (meal.getName() == null || meal.getName().isEmpty()) {
            throw new IllegalArgumentException("Meal name is null or empty");
        }

        if (meal.getPrice() == null || meal.getPrice() < 0) {
            throw new IllegalArgumentException("Meal price is null or negative");
        }

        if (meal.getRestaurant() == null) {
            throw new IllegalArgumentException("Meal restaurant is null");
        }

        restaurantValidator.validate(meal.getRestaurant());
    }
}
