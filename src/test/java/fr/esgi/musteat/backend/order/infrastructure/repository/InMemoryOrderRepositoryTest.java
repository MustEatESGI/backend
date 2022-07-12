package fr.esgi.musteat.backend.order.infrastructure.repository;

import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.domain.OrderRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InMemoryOrderRepositoryTest implements OrderRepository {

    private final List<Order> orders;

    public InMemoryOrderRepositoryTest() {
        this.orders = new ArrayList<>();
    }


    @Override
    public Optional<Order> get(Long key) {
        if (orders.size() > key) {
            return Optional.of(orders.get(key.intValue()));
        }
        return Optional.empty();
    }

    @Override
    public Long add(Order value) {
        orders.add(value);
        return (long) orders.indexOf(value);
    }

    @Override
    public boolean update(Order value) {
        if (orders.size() > value.getId()) {
            orders.set(value.getId().intValue(), value);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (orders.size() > value) {
            orders.remove(value.intValue());
            return true;
        }
        return false;
    }

    @Override
    public List<Order> getAll() {
        return Collections.unmodifiableList(orders);
    }
}
