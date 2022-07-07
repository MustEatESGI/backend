package fr.esgi.musteat.backend.order.domain;

import fr.esgi.musteat.backend.kernel.Validator;

public class OrderValidator implements Validator<Order> {
    @Override
    public void validate(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order is null");
        }

        if (order.getOrderDate() == null) {
            throw new IllegalArgumentException("Order date is null");
        }
    }
}
