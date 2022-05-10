package fr.esgi.musteat.backend.meal.domain;

import fr.esgi.musteat.backend.kernel.Repository;

import java.util.List;

public interface MealRepository extends Repository<Meal, Long> {
    List<Meal> getAllByRestaurantId(Long restaurantId);
}
