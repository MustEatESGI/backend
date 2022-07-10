package fr.esgi.musteat.backend.order.infrastructure.service;

import fr.esgi.musteat.backend.kernel.Repository;
import fr.esgi.musteat.backend.kernel.Service;
import fr.esgi.musteat.backend.kernel.ServiceTest;
import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.domain.OrderRepository;
import fr.esgi.musteat.backend.order.domain.OrderValidator;
import fr.esgi.musteat.backend.order.infrastructure.repository.InMemoryOrderRepositoryTest;

import java.time.LocalDateTime;

public class OrderServiceTest extends ServiceTest<OrderRepository, Order, Long> {

    public OrderServiceTest() {
        super(new Order(0L, LocalDateTime.MIN, null, null), new Order(0L, LocalDateTime.now(), null, null));
    }

    @Override
    protected OrderRepository getRepository() {
        return new InMemoryOrderRepositoryTest();
    }

    @Override
    protected Service<OrderRepository, Order, Long> getService(Repository repository) {
        return new OrderService((OrderRepository) repository, new OrderValidator());
    }
}
