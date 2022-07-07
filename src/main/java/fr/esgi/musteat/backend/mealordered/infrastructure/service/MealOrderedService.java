package fr.esgi.musteat.backend.mealordered.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.mealordered.domain.MealOrdered;
import fr.esgi.musteat.backend.mealordered.domain.MealOrderedRepository;

@org.springframework.stereotype.Service
public class MealOrderedService extends Service<MealOrderedRepository, MealOrdered, Long> {

    public MealOrderedService(MealOrderedRepository repository, Validator<MealOrdered> validator) {
        super(repository, validator, "meal ordered");
    }
}
