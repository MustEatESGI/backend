package fr.esgi.musteat.backend.meal.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.meal.domain.Meal;
import fr.esgi.musteat.backend.meal.domain.MealRepository;

@org.springframework.stereotype.Service
public class MealService extends Service<MealRepository, Meal, Long> {

    public MealService(MealRepository repository, Validator<Meal> validator) {
        super(repository, validator, "meal");
    }
}
