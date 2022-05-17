package fr.esgi.musteat.backend.meal.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealRepository;
import fr.esgi.musteat.backend.meal.infrastructure.repository.InDBMealRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class MealService extends Service<MealRepository, Meal, Long> {

    private final MealRepository mealRepository;

    public MealService(MealRepository repository, Validator<Meal> validator) {
        super(repository, validator, "meal");
        this.mealRepository = repository;
    }

    public List<Meal> findByRestaurantId(Long restaurantId) {
        return mealRepository.getAllByRestaurantId(restaurantId);
    }

    public List<Meal> findByName(String name) {
        return mealRepository.getAllByName(name);
    }
}
