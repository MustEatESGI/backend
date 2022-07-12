package fr.esgi.musteat.backend.mealordered.domain;

import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.order.domain.OrderValidator;

public class MealOrderedValidator implements Validator<MealOrdered> {

    private final OrderValidator orderValidator = new OrderValidator();

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

        if (mealOrdered.getOrder() == null) {
            throw new IllegalArgumentException("MealOrdered order is null");
        }

        orderValidator.validate(mealOrdered.getOrder());
    }
}
