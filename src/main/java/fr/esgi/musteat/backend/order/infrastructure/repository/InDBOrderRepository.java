package fr.esgi.musteat.backend.order.infrastructure.repository;

import fr.esgi.musteat.backend.order.domain.Order;
import fr.esgi.musteat.backend.order.domain.OrderRepository;
import fr.esgi.musteat.backend.order.infrastructure.database.OrderDB;
import fr.esgi.musteat.backend.order.infrastructure.database.OrderDBRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InDBOrderRepository implements OrderRepository {

    private final OrderDBRepository dbRepository;

    public InDBOrderRepository(OrderDBRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    @Override
    public Optional<Order> get(Long key) {
        Optional<OrderDB> orderDB = dbRepository.findById(key);
        return orderDB.map(OrderDB::toOrder);
    }

    @Override
    public Long add(Order value) {
        OrderDB orderDB = dbRepository.save(OrderDB.fromOrder(value));
        value.setId(orderDB.getId());
        return orderDB.getId();
    }

    @Override
    public boolean update(Order value) {
        if (dbRepository.findById(value.getId()).isPresent()) {
            dbRepository.save(OrderDB.fromOrder(value));
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Long value) {
        if (dbRepository.findById(value).isPresent()) {
            dbRepository.delete(dbRepository.findById(value).get());
            return true;
        }
        return false;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        dbRepository.findAll().forEach(orderDB -> orders.add(orderDB.toOrder()));
        return orders;
    }
}
