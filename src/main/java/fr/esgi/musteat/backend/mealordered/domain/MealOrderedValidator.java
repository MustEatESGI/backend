package fr.esgi.musteat.backend.mealordered.domain;

import fr.esgi.musteat.backend.kernel.Validator;

public class MealOrderedValidator implements Validator<MealOrdered> {
    @Override
    public void validate(MealOrdered mealOrdered) {
        if (mealOrdered == null) {
            throw new IllegalArgumentException("MealOrdered is null");
        }

        if (mealOrdered.getName() == null || mealOrdered.getName().isEmpty()) {
            throw new IllegalArgumentException("MealOrdered name is null or empty");
        }

        if (mealOrdered.getPrice() == null || mealOrdered.getPrice() < 0) {
            throw new IllegalArgumentException("MealOrdered price is null or negative");
        }
    }
}
