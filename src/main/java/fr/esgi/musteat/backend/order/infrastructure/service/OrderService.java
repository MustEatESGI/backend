package fr.esgi.musteat.backend.order.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.Validator;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.domain.OrderRepository;

@org.springframework.stereotype.Service
public class OrderService extends Service<OrderRepository, Order, Long> {

    public OrderService(OrderRepository repository, Validator<Order> validator) {
        super(repository, validator, "order");
    }
}
